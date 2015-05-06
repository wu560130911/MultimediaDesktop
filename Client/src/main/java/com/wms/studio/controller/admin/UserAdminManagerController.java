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

package com.wms.studio.controller.admin;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.user.OnlineUser;
import com.wms.studio.api.dto.user.UserInfoDto;
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
public class UserAdminManagerController {

	@Resource
	private UserService userService;

	@Resource
	private SessionDAO sessionDao;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSessionDao(SessionDAO sessionDao) {
		this.sessionDao = sessionDao;
	}

	@RequestMapping("admin/userManager/userStore")
	public void getUserStore(Model model, PageSize pageSize) {
		PageDto<UserInfoDto> users = userService.listUserInfos(pageSize, null);
		model.addAttribute("users", users.getValues());
		model.addAttribute("total", users.getTotalElements());
	}

	@RequestMapping("admin/userManager/userOnlineStore")
	public void getOnlineUsers(Model model) {
		Iterator<Session> sessions = sessionDao.getActiveSessions().iterator();
		ArrayList<OnlineUser> ous = new ArrayList<OnlineUser>();
		while (sessions.hasNext()) {
			OnlineUser ou = new OnlineUser();
			SimpleSession session = (SimpleSession) sessions.next();
			ou.setHost(session.getHost());
			ou.setId(session.getId().toString());
			ou.setLastAccessTime(session.getLastAccessTime());
			ou.setStartTime(session.getStartTimestamp());
			PrincipalCollection principal = (PrincipalCollection) session
					.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (principal != null) {
				ShiroUser su = (ShiroUser) principal.getPrimaryPrincipal();
				ou.setUserid(su.loginName);
				ou.setUsername(su.name);
				ou.setLogin(true);
			}
			ous.add(ou);
		}
		model.addAttribute("users", ous);
		model.addAttribute("total", ous.size());
	}

	@RequestMapping("admin/userManager/forceLogout")
	public void forceLogout(Model model, @RequestBody JSONArray jsonArray) {

		try {
			if (jsonArray == null || jsonArray.size() <= 0) {
				throw new VerificationException("参数错误");
			}

			for (int i = 0; i < jsonArray.size(); i++) {
				String sessionId = jsonArray.getJSONObject(i).getString("id");
				if (StringUtils.isNotBlank(sessionId)) {
					try {
						Session session = sessionDao.readSession(sessionId);
						if (session != null) {
							session.setAttribute(Constant.USER_STATUS_KEY, true);
							sessionDao.update(session);
						}
					} catch (UnknownSessionException e) {

					}

				}
			}
			model.addAttribute("success", true);
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}

	}
}
