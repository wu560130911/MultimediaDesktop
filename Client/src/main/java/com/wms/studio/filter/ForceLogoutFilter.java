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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.wms.studio.constant.Constant;

public class ForceLogoutFilter extends AccessControlFilter {

	private static final String FORCE_LOGOUT_SIGN = Constant.USER_STATUS_KEY
			+ "=" + Constant.USER_FORACE_STATUS;

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object object) throws Exception {
		Session session = getSubject(request, response).getSession(false);
		if (session == null) {
			return true;
		}
		return session.getAttribute(Constant.USER_STATUS_KEY) == null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		try {
			getSubject(request, response).logout();
		} catch (Exception e) {

		}
		String loginUrl = getLoginUrl()
				+ (getLoginUrl().contains("?") ? "&" : "?") + FORCE_LOGOUT_SIGN;
		WebUtils.issueRedirect(request, response, loginUrl);
		return false;
	}

}
