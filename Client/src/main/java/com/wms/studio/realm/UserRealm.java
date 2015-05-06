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

package com.wms.studio.realm;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.user.UserLoginDto;
import com.wms.studio.api.service.UserService;
import com.wms.studio.exception.UnValidationAccountException;
import com.wms.studio.exception.ValidateCodeException;
import com.wms.studio.token.SystemLoginToken;
import com.wms.studio.utils.Encodes;

/**
 * @author WMS
 * 
 */
public class UserRealm extends AuthorizingRealm {

	private static final Logger log = Logger.getLogger(UserRealm.class);

	@Autowired
	private UserService userservice;

	public void setUserservice(UserService userservice) {
		this.userservice = userservice;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		UserRole role = shiroUser.role;
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		info.addRole(UserRole.用户.getRole());
		
		if(UserRole.开发者.equals(role)){
			info.addRole(UserRole.开发者.getRole());
		}
		
		if(UserRole.管理员.equals(role)){
			info.addRole(UserRole.开发者.getRole());
			info.addRole(UserRole.管理员.getRole());
		}
		return info;
	}

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {

		SystemLoginToken token = (SystemLoginToken) authcToken;

		if (token.getUsername() == null) {
			throw new AccountException("提交表单未包含用户名.");
		}

		// 增加判断验证码逻辑
		String captcha = token.getCaptcha();
		String exitCode = (String) SecurityUtils
				.getSubject()
				.getSession()
				.getAttribute(
						com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
			throw new ValidateCodeException("验证码错误");
		}

		UserLoginDto user = userservice.login(token.getUsername());

		if (user == null) {
			return null;
		}

		log.info("[用户登录]-[获取登录用户信息]-返回数据结果:"
				+ ToStringBuilder.reflectionToString(user));

		if (user != null && UserConstant.SUCCESS == user.getResult()) {

			// 用户没有被验证
			if (!user.isvStatus()) {
				log.info("用户没有通过邮箱验证.");
				throw new UnValidationAccountException();
			}
			
			if(user.isDisable()&&UserDisableReason.登录超过限制.equals(user.getDisableReason())){
				throw new LockedAccountException();
			}

			// 用户被锁定
			if (user.isDisable()) {
				log.info("用户被禁止登录.");
				throw new DisabledAccountException();
			}

			byte[] salt = Encodes.decodeHex(user.getSalt());

			return new SimpleAuthenticationInfo(new ShiroUser(user.getId(),
					user.getName(), user.getRole()), user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
		}
		throw new UnknownAccountException();
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				UserConstant.HASH_ALGORITHM);
		matcher.setHashIterations(UserConstant.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	@SuppressWarnings("serial")
	public static class ShiroUser implements Serializable {
		public String loginName;
		public String name;
		public UserRole role;

		public ShiroUser(String loginName, String name, UserRole role) {
			this.loginName = loginName;
			this.name = name;
			this.role = role;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public UserRole getRole() {
			return role;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		super.setCacheManager(cacheManager);
	}

	@Override
	public void setAuthenticationTokenClass(
			Class<? extends AuthenticationToken> authenticationTokenClass) {
		super.setAuthenticationTokenClass(authenticationTokenClass);
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
}
