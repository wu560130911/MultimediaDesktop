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

package com.wms.studio.controller.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.OrderDto;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.dto.sort.SortDto.Direction;
import com.wms.studio.api.service.ApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class AppManagerController {

	private static final Logger log = Logger
			.getLogger(AppManagerController.class);

	@Resource
	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@RequestMapping("/app/getMyTreeStore")
	public void getAppTreeStoreNavigate(HttpServletRequest request,
			HttpServletResponse response) {

		String url = "/data/json/none.json";
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject == null) {
				throw new VerificationException();
			}
			if (subject.hasRole(UserRole.管理员.getRole())) {
				url = "/data/json/AppTreeStore/admin.json";
			} else {
				url = "/data/json/AppTreeStore/user.json";
			}
		} catch (VerificationException e) {

		}
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException | IOException e) {
			log.fatal("跳转异常", e);
		}

	}

	@RequestMapping(value = "/app/addApp", method = RequestMethod.POST)
	public void addApplication(@Valid ApplicationDto dto, Model model,
			BindingResult result) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "用户未登录");
			dto.setUserId(userId);
			CommonResponseDto response = applicationService.addApplication(dto);
			if (response.getResult() == UserConstant.SUCCESS) {
				model.addAttribute("success", true);
			} else {
				throw new VerificationException(response.getErrorMessage());
			}
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
		dto = null;// 防止返回给客户端
	}

	@RequestMapping(value = "/app/listAllApp")
	public void listAllApplication(Model model, Integer page, Integer limit,
			String userId, String name, ApplicationType typeGroup,
			UserRole role,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		ArrayList<UserRole> roles = new ArrayList<>();

		if (role == null) {
			roles.add(UserRole.用户);
			Subject subject = SecurityUtils.getSubject();
			if (subject.hasRole(UserRole.开发者.getRole())) {
				roles.add(UserRole.开发者);
			} else if (subject.hasRole(UserRole.管理员.getRole())) {
				roles.add(UserRole.管理员);
			}
		} else {
			roles.add(role);
		}

		PageDto<ApplicationDto> apps = applicationService.findBy(userId, name,
				startDate, endDate, typeGroup, roles, Boolean.TRUE,
				new PageSize(page, limit), null);
		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}

	@RequestMapping(value = "/app/listUnCheck")
	public void listAllUnCheckApplication(Model model, Integer page, Integer limit) {
		String userId = UserUtils.getCurrentUserId();
		PageDto<ApplicationDto> apps = applicationService.findBy(userId, null,
				null, null, null, null, Boolean.FALSE,
				new PageSize(page, limit),null);
		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}
	
	@RequestMapping(value = "/app/listCheck")
	public void listAllCheckApplication(Model model, Integer page, Integer limit) {
		String userId = UserUtils.getCurrentUserId();
		PageDto<ApplicationDto> apps = applicationService.findBy(userId, null,
				null, null, null, null, Boolean.TRUE,
				new PageSize(page, limit),null);
		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}
	
	@RequestMapping(value = "/app/listHotApp")
	public void listAllApplication(Model model, Integer page, Integer limit) {
		ArrayList<UserRole> roles = new ArrayList<>();
		roles.add(UserRole.用户);
		Subject subject = SecurityUtils.getSubject();
		if (subject.hasRole(UserRole.开发者.getRole())) {
			roles.add(UserRole.开发者);
		} else if (subject.hasRole(UserRole.管理员.getRole())) {
			roles.add(UserRole.管理员);
		}
		OrderDto order = new OrderDto(Direction.DESC, "useCount");
		PageDto<ApplicationDto> apps = applicationService.findBy(null, null,
				null, null, null, roles, Boolean.TRUE,
				new PageSize(page, limit), new SortDto(order));
		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}
}
