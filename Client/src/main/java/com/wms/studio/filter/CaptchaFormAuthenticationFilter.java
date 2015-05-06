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

package com.wms.studio.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.wms.studio.api.constant.LoginType;
import com.wms.studio.api.dto.user.LoginIpDto;
import com.wms.studio.api.service.LoginIpService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.UnValidationAccountException;
import com.wms.studio.exception.ValidateCodeException;
import com.wms.studio.realm.UserRealm.ShiroUser;
import com.wms.studio.token.SystemLoginToken;
import com.wms.studio.utils.AddressUtil;

/**
 * @author WMS
 * 
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

	private static final Logger log = Logger
			.getLogger(CaptchaFormAuthenticationFilter.class);

	private static String KAPTCHA_REQUEST_KEY = "captcha";

	private LoginIpService loginIpService;

	public void setLoginIpService(LoginIpService loginIpService) {
		this.loginIpService = loginIpService;
	}

	/*
	 * 主要是针对登入成功的处理方法。对于请求头是AJAX的之间返回JSON字符串。
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// 登录成功日志
		logLoginStatus(httpServletRequest, LoginType.登录成功);

		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			issueSuccessRedirect(request, response);
		} else {
			httpServletResponse.setCharacterEncoding("UTF-8");
			PrintWriter out = httpServletResponse.getWriter();
			out.println("{success:true,message:'登录成功'}");
			out.flush();
			out.close();
		}
		return false;
	}

	/**
	 * 主要是处理登入失败的方法
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		setFailureAttribute(request, e);
		if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request)
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			return true;
		}
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.printf("{success:false,message:'%s'}",
					request.getAttribute(getFailureKeyAttribute()));
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * 所有请求都会经过的方法。
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {

		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {

				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}

				return executeLogin(request, response);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {

			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}
			if (!"XMLHttpRequest"
					.equalsIgnoreCase(((HttpServletRequest) request)
							.getHeader("X-Requested-With"))) {// 不是ajax请求
				saveRequestAndRedirectToLogin(request, response);
			} else {
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{message:'login'}");
				out.flush();
				out.close();
			}
			return false;
		}
	}

	protected void setFailureAttribute(ServletRequest request,
			AuthenticationException ae) {
		String errorMessage = null;

		if (ae instanceof IncorrectCredentialsException) {
			errorMessage = "密码错误,输入错误超过当日限制,将锁定账户";
			// 登录失败日志记录
			logLoginStatus(request, LoginType.登录失败);
		} else if (ae instanceof ValidateCodeException) {
			errorMessage = "验证码错误";
		} else if (ae instanceof UnValidationAccountException) {
			errorMessage = "账号未被验证";
		} else if (ae instanceof LockedAccountException) {
			errorMessage = "密码输入错误超过当日限制,请明天再试";
		} else if (ae instanceof DisabledAccountException) {
			errorMessage = "账号被管理员锁定";
		} else if (ae instanceof UnknownAccountException) {
			errorMessage = "账号不存在";
		} else {
			errorMessage = "未知错误";
			log.fatal("登录错误-未知错误，请管理员检查", ae);
		}

		request.setAttribute(getFailureKeyAttribute(), errorMessage);
	}

	protected AuthenticationToken createToken(

	ServletRequest request, ServletResponse response) {

		String username = getUsername(request);

		String password = getPassword(request);

		String captcha = getCaptcha(request);

		boolean rememberMe = isRememberMe(request);

		String host = getHost(request);

		return new SystemLoginToken(username, password.toCharArray(),
				rememberMe, host, captcha);

	}

	protected String getCaptcha(ServletRequest request) {

		return WebUtils.getCleanParam(request, KAPTCHA_REQUEST_KEY);

	}

	private void logLoginStatus(ServletRequest request, LoginType loginType) {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null) {
			return;
		}
		ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
		String userId = null;
		if (shiroUser == null && LoginType.登录失败.equals(loginType)) {
			userId = request
					.getParameter(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		} else {
			userId = shiroUser.loginName;
		}
		if (StringUtils.isBlank(userId)) {
			return;
		}
		LoginIpDto loginIpDto = new LoginIpDto(userId,
				AddressUtil.getIpAddr((HttpServletRequest) request), loginType);
		loginIpService.addLoginIp(loginIpDto);
	}
}
