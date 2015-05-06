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

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.wms.studio.constant.Constant;
import com.wms.studio.realm.UserRealm.ShiroUser;

/**
 * @author WMS
 * 
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

	private static final String KICKOUT_SIGN = Constant.USER_STATUS_KEY + "="
			+ Constant.USER_KICKOUT_STATUS;
	private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
	private int maxSession = 1; // 同一个帐号最大会话数 默认1
	private Cache<String, Deque<Serializable>> cache;
	private SessionManager sessionManager;

	public boolean isKickoutAfter() {
		return kickoutAfter;
	}

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			// 如果没有登录，直接进行之后的流程
			return true;
		}
		Session session = subject.getSession();

		ShiroUser user = (ShiroUser) subject.getPrincipal();
		Serializable sessionId = session.getId();

		Deque<Serializable> deque = cache.get(user.loginName);
		if (deque == null) {
			deque = new ArrayDeque<Serializable>();
			cache.put(user.loginName, deque);
		}

		// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
		if (!deque.contains(sessionId)
				&& session.getAttribute("kickout") == null) {
			deque.push(sessionId);
			cache.put(user.loginName, deque);
		}

		// 如果队列里的sessionId数超出最大会话数，开始踢人
		if (deque.size() > maxSession) {
			// 更新缓存
			Iterator<Serializable> loginUsers = deque.iterator();
			while (loginUsers.hasNext()) {
				Serializable ss = loginUsers.next();
				try {
					sessionManager.getSession(new DefaultSessionKey(ss));
				} catch (SessionException e) {
					deque.remove(ss);
					cache.put(user.loginName, deque);
				}
			}
			// 缓存更新完成后，还是超出规定人数时，开始踢人
			while (deque.size() > maxSession) {
				Serializable kickoutSessionId = null;
				if (kickoutAfter) { // 如果踢出后者
					kickoutSessionId = deque.removeFirst();
				} else { // 否则踢出前者
					kickoutSessionId = deque.removeLast();
				}
				try {
					Session kickoutSession = sessionManager
							.getSession(new DefaultSessionKey(kickoutSessionId));
					if (kickoutSession != null) {
						// 设置会话的kickout属性表示踢出了
						kickoutSession.setAttribute(Constant.USER_STATUS_KEY,
								true);
					}
				} catch (Exception e) {// ignore exception
				}
			}
			cache.put(user.loginName, deque);
		}

		// 如果被踢出了，直接退出，重定向到踢出后的地址
		if (session.getAttribute(Constant.USER_STATUS_KEY) != null) {
			// 会话被踢出了
			try {
				subject.logout();
			} catch (Exception e) { // ignore
			}
			String loginUrl = getLoginUrl()
					+ (getLoginUrl().contains("?") ? "&" : "?") + KICKOUT_SIGN;
			WebUtils.issueRedirect(request, response, loginUrl);
			return false;
		}
		return true;
	}

}
