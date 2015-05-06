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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.api.dto.user.UserDto;
import com.wms.studio.api.service.UserService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.constant.Constant;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.realm.UserRealm.ShiroUser;
import com.wms.studio.security.utils.Digests;
import com.wms.studio.utils.AddressUtil;
import com.wms.studio.utils.Encodes;
import com.wms.studio.utils.MemCacheUtil;

/**
 * @author WMS
 * 
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.GET)
	public String registerGetPage(Model model) {
		model.addAttribute("tab", 2);
		return "login";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ModelAndView register(ModelAndView model, UserDto user,
			String passwordAgain, String captcha, HttpSession session,
			HttpServletRequest request) {

		try {
			model.addObject("user", user);
			if (StringUtils.isBlank(captcha)) {
				throw new VerificationException("验证码不允许为空");
			}
			String code = (String) session
					.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(captcha)) {
				throw new VerificationException("验证码错误");
			}

			if (StringUtils.isBlank(passwordAgain)
					|| StringUtils.isBlank(user.getPassword())
					|| !passwordAgain.equals(user.getPassword())) {
				throw new VerificationException("密码不允许为空或者两次密码输入不匹配");
			}
			user.setRegisterAddress(AddressUtil.getIpAddr(request));
			CommonResponseDto dto = userService.registerUser(user);

			if (UserConstant.SUCCESS != dto.getResult()) {
				throw new VerificationException(dto.getErrorMessage());
			}
			model.addObject(Constant.USER_STATUS_KEY,
					Constant.USER_REGISTER_STATUS);
			model.setViewName("onlineStatus");

		} catch (VerificationException e) {
			// 注册失败异常
			model.addObject(
					FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
					e.getMessage());
			model.addObject("tab", "2");
			model.setViewName("login");
		}
		return model;
	}

	@RequestMapping(value = "/user/findPassword/{userId}", method = RequestMethod.POST)
	public void findPassword(String userIdDigests,
			@PathVariable("userId") String userId, String password,
			String rpassword, String captcha, HttpSession session,
			ModelAndView model) {

		try {

			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidString(userIdDigests, "参数错误");
			StringUtils.isValidString(password, "新密码不允许为空");
			StringUtils.isValidString(rpassword, "新密码不允许为空");
			StringUtils.isValidString(captcha, "验证码不允许为空");

			String code = (String) session
					.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(captcha)) {
				throw new VerificationException("验证码错误");
			}

			if (!password.equals(rpassword)) {
				throw new VerificationException("两次密码输入不一致");
			}

			String digests = Encodes.encodeHex(Digests.sha1(userId.getBytes()));

			if (!userIdDigests.equals(digests)) {
				// 这儿跳转到错误页面
				model.setViewName("onlineStatus");
				model.addObject(Constant.USER_STATUS_KEY,
						Constant.USER_FIND_PASSWORD_ERROR);
				return;
			}

			CommonResponseDto responseDto = userService.findPassword(userId,
					rpassword);

			if (UserConstant.SUCCESS != responseDto.getResult()) {
				throw new VerificationException(responseDto.getErrorMessage());
			}

			model.setViewName("redirect:/user/login");

		} catch (VerificationException e) {
			model.addObject(
					FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
					e.getMessage());
			model.setViewName("findPassword");
			if(userId!=null){
				model.addObject("userId", userId);
				model.addObject("userIdDigests",
						Encodes.encodeHex(Digests.sha1(userId.getBytes())));
			}
		}

	}

	@RequestMapping(value = "/user/valid/{userId}", method = RequestMethod.GET)
	public ModelAndView validEmail(@PathVariable("userId") String userId,
			@RequestParam("p") String value, ModelAndView model) {

		try {
			model.setViewName("onlineStatus");
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(value)) {
				throw new VerificationException("参数错误");
			}

			CommonResponseDto responseDto = userService.checkEmailForUser(
					userId, value);

			if (responseDto == null) {
				throw new VerificationException("参数错误");
			}

			if (UserConstant.SUCCESS != responseDto.getResult()) {
				throw new VerificationException(responseDto.getErrorMessage());
			}

			// 类型验证
			// 1、绑定邮箱成功
			if (EmailConstant.EMAIL_USER_REGISTER.equals(responseDto
					.getErrorMessage())) {
				model.addObject(Constant.USER_STATUS_KEY,
						Constant.USER_VALID_EMAIL_SUCCESS);
			} else if (EmailConstant.EMAIL_USER_CHANGE_EMAIL.equals(responseDto
					.getErrorMessage())) {
				model.addObject(Constant.USER_STATUS_KEY,
						Constant.EMAIL_USER_CHANGE_EMAIL_SUCCESS);
			} else if (EmailConstant.EMAIL_USER_FIND_PASSWORD
					.equals(responseDto.getErrorMessage())) {
				model.setViewName("findPassword");
				model.addObject("userId", userId);
				model.addObject("userIdDigests",
						Encodes.encodeHex(Digests.sha1(userId.getBytes())));
			}

		} catch (VerificationException e) {
			model.addObject(
					FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME,
					e.getMessage());
			model.addObject(Constant.USER_STATUS_KEY,
					Constant.USER_VALID_EMAIL_FAIL);
		}

		return model;
	}

	@RequestMapping(value = "/user/loginInfo", method = RequestMethod.POST)
	public void userLoginInfo(Model model) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
			WallpaperDto wallpaper = MemCacheUtil
					.getThemeCache(shiroUser.loginName);
			if (wallpaper == null) {
				wallpaper = userService.getUserTheme(shiroUser.loginName);
				if (wallpaper.getResult() == UserConstant.SUCCESS) {
					MemCacheUtil.setThemeCache(shiroUser.loginName, wallpaper);
				} else {
					wallpaper = null;
				}
			}
			if (wallpaper != null) {
				model.addAttribute("theme", wallpaper);
			}
			model.addAttribute("user", shiroUser);
		}
	}
}
