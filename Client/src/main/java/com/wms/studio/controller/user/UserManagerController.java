/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wms.studio.controller.user;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.user.CreditDto;
import com.wms.studio.api.dto.user.LoginIpInfoDto;
import com.wms.studio.api.dto.user.UpdateUserDto;
import com.wms.studio.api.dto.user.UserInfoDto;
import com.wms.studio.api.service.CreditService;
import com.wms.studio.api.service.LoginIpService;
import com.wms.studio.api.service.UserService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class UserManagerController {

	private static final Logger log = Logger
			.getLogger(UserManagerController.class);

	@Resource
	private UserService userService;

	@Resource
	private CreditService creditService;

	@Resource
	private LoginIpService loginIpService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setCreditService(CreditService creditService) {
		this.creditService = creditService;
	}

	public void setLoginIpService(LoginIpService loginIpService) {
		this.loginIpService = loginIpService;
	}

	@RequestMapping("/userManager/getMyTreeStore")
	public void getUserTreeStoreNavigate(HttpServletRequest request,
			HttpServletResponse response) {

		String url = "/data/json/none.json";
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject == null) {
				throw new VerificationException();
			}
			if (subject.hasRole(UserRole.管理员.getRole())) {
				url = "/data/json/UserTreeStore/admin.json";
			} else {
				url = "/data/json/UserTreeStore/user.json";
			}
		} catch (VerificationException e) {

		}
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException | IOException e) {
			log.fatal("跳转异常", e);
		}

	}

	@RequestMapping("/userManager/userInfo")
	public void getUserInfo(Model model) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			UserInfoDto userInfo = userService.getUserInfo(userId);
			StringUtils.isValidObject(userInfo, "获取用户信息失败");
			model.addAttribute("success", true);
			model.addAttribute("data", userInfo);
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}

	@RequestMapping("/userManager/modify")
	public void ModifyUser(Model model, UpdateUserDto updateUserDto) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			updateUserDto.setId(userId);

			CommonResponseDto response = userService
					.updateUserInfo(updateUserDto);
			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			}
			model.addAttribute("success", true);
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}

	@RequestMapping("/userManager/modifyPassword")
	public void ModifyUserPassword(Model model, UpdateUserDto updateUserDto,
			String rModifyPassword) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			StringUtils.isValidObject(updateUserDto, "参数错误");
			StringUtils.isValidObject(rModifyPassword, "参数错误");

			if (!StringUtils.equals(rModifyPassword,
					updateUserDto.getModifyPassword())) {
				throw new VerificationException("两次密码不一致");
			}

			updateUserDto.setId(userId);

			CommonResponseDto response = userService
					.changeUserPassword(updateUserDto);
			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			}
			// 需要重新登录
			SecurityUtils.getSubject().logout();
			model.addAttribute("success", true);
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}

	@RequestMapping("/userManager/modifyEmail")
	public void ModifyUserEmail(Model model, UpdateUserDto updateUserDto) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			StringUtils.isValidObject(updateUserDto, "参数错误");

			updateUserDto.setId(userId);

			CommonResponseDto response = userService.changeEmail(updateUserDto);
			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			}
			model.addAttribute("success", true);
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}

	@RequestMapping("/userManager/getCreditStore")
	public void getCreditStore(Model model,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			PageSize pageSize) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			PageDto<CreditDto> credits = creditService.findBy(userId,
					startDate, endDate, pageSize);
			if (credits == null) {
				throw new VerificationException("获取失败");
			}
			model.addAttribute("success", true);
			model.addAttribute("credits", credits.getValues());
			model.addAttribute("total", credits.getTotalElements());
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}

	@RequestMapping("/userManager/getLoginLogStore")
	public void getLoginLogStore(Model model,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			PageSize pageSize) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			PageDto<LoginIpInfoDto> logins = loginIpService.findBy(userId,
					startDate, endDate, pageSize);
			if (logins == null) {
				throw new VerificationException("获取失败");
			}
			model.addAttribute("success", true);
			model.addAttribute("logins", logins.getValues());
			model.addAttribute("total", logins.getTotalElements());
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}
}
