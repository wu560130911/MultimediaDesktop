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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.service.UserService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.constant.Constant;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.realm.UserRealm.ShiroUser;

/**
 * @author WMS
 * 
 */
@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public String login(
			@RequestParam(value = Constant.USER_STATUS_KEY, defaultValue = Constant.USER_NORMAL_STATUS) String userStatus,
			Model model) {

		Subject subject = SecurityUtils.getSubject();

		if (subject != null && subject.isAuthenticated()) {
			return "redirect:/index";
		}

		// 暂时没有业务逻辑进行处理
		if (!Constant.USER_NORMAL_STATUS.equals(userStatus)) {
			model.addAttribute(Constant.USER_STATUS_KEY, userStatus);
			// return "redirect:/user/onlineStatus";
			return "onlineStatus";
		}

		// 获取记住账号的用户信息
		if (subject != null && !subject.isAuthenticated()
				&& subject.isRemembered()) {
			ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
			if (shiroUser != null) {
				// 用户名
				model.addAttribute(
						FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
						shiroUser.loginName);
			}
		}

		return "login";
	}

	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();

		if (subject != null && subject.isAuthenticated()) {
			subject.logout();
		}
		return "login";
	}

	@RequestMapping(value = "/user/onlineStatus")
	public String onlineStatus(Model model,
			@RequestParam(value = Constant.USER_STATUS_KEY) String userStatus) {

		// 暂时没有业务逻辑进行处理

		model.addAttribute(Constant.USER_STATUS_KEY,
				Constant.USER_NORMAL_STATUS);

		return "onlineStatus";
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public String fail(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			HttpServletRequest request, Model model) {
		// 用户名
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		// 登录失败异常
		model.addAttribute(
				FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
				request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME));
		return "login";
	}

	@RequestMapping(value = "/user/remember")
	public @ResponseBody
	ShiroUser remember() {

		Subject subject = SecurityUtils.getSubject();
		ShiroUser shiroUser = null;
		if (!subject.isAuthenticated() && subject.isRemembered()) {
			shiroUser = (ShiroUser) subject.getPrincipal();
		}

		return shiroUser;
	}

	@RequestMapping(value = "/user/findPassword", method = RequestMethod.POST)
	public ModelAndView findPassword(String email, String captcha,
			HttpSession session, ModelAndView model) {

		try {
			model.setViewName("onlineStatus");
			if (StringUtils.isBlank(captcha)) {
				throw new VerificationException("验证码不允许为空");
			}
			String code = (String) session
					.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(captcha)) {
				throw new VerificationException("验证码错误");
			}

			if (!StringUtils.checkEmail(email)) {
				throw new VerificationException("邮箱错误");
			}

			CommonResponseDto response = userService.findPassword(email);

			if (response.getResult() == UserConstant.SUCCESS) {
				model.addObject(Constant.USER_STATUS_KEY,
						Constant.USER_FIND_PASSWORD_SUCCESS);
			} else {
				throw new VerificationException(response.getErrorMessage());
			}

		} catch (VerificationException e) {
			// 注册失败异常
			model.addObject(
					FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
					e.getMessage());
			model.addObject(Constant.USER_STATUS_KEY,
					Constant.USER_FIND_PASSWORD_FAIL);
		}
		return model;
	}
}
