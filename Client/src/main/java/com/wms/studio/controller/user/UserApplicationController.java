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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.ListDto;
import com.wms.studio.api.dto.app.UserAppDto;
import com.wms.studio.api.dto.app.UserAppIconDto;
import com.wms.studio.api.dto.app.UserApplicationDto;
import com.wms.studio.api.service.UserApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class UserApplicationController {

	@Resource
	private UserApplicationService userApplicationService;

	public void setUserApplicationService(
			UserApplicationService userApplicationService) {
		this.userApplicationService = userApplicationService;
	}

	@RequestMapping(value = "/userApp/addApp", method = RequestMethod.POST)
	public void addApplication(Model model, Boolean desktopIcon,
			Boolean quickStart, Boolean startMenu, Long id) {
		try {
			model.addAttribute("success", false);
			if (id == null || id < 1) {
				throw new VerificationException("参数错误");
			}

			String userId = UserUtils.getCurrentUserId();

			if (userId == null) {
				throw new VerificationException("账号没有登录");
			}

			CommonResponseDto response = userApplicationService.addApplication(
					userId, id, quickStart, desktopIcon, startMenu);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}
		} catch (VerificationException e) {
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/getAppDesktop", method = RequestMethod.POST)
	public void getAppDesktop(Model model) {
		try {
			model.addAttribute("success", false);

			String userId = UserUtils.getCurrentUserId();

			if (userId == null) {
				throw new VerificationException("账号没有登录");
			}

			ListDto<UserAppDto> response = userApplicationService
					.findDesktopUserApp(userId);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("apps", response.getData());
			}
		} catch (VerificationException e) {
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/getAppQuickStart", method = RequestMethod.POST)
	public void getAppQuickStart(Model model) {
		try {
			model.addAttribute("success", false);

			String userId = UserUtils.getCurrentUserId();

			if (userId == null) {
				throw new VerificationException("账号没有登录");
			}

			ListDto<UserAppDto> response = userApplicationService
					.findQuickStartUserApp(userId);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("apps", response.getData());
			}
		} catch (VerificationException e) {
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/getAppStartMenu", method = RequestMethod.POST)
	public void getAppStartMenu(Model model) {
		try {
			model.addAttribute("success", false);

			String userId = UserUtils.getCurrentUserId();

			if (userId == null) {
				throw new VerificationException("账号没有登录");
			}

			ListDto<UserAppDto> response = userApplicationService
					.findStartMenuUserApp(userId);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("apps", response.getData());
			}
		} catch (VerificationException e) {
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/changeAppIcon", method = RequestMethod.POST)
	public void changeAppIcon(Model model, Integer type, Integer id) {
		// type==1代表增加到快速启动栏
		// type==2代表增加到开始菜单
		// type==4代表增加到桌面
		// type==3代表增加到快速启动栏和开始菜单
		// type==5代表增加到快速启动栏和桌面
		// type==6代表增加到开始菜单和桌面
		// type==7代表增加到快速启动栏和开始菜单和桌面
		try {

			StringUtils.isValidObject(type, "参数错误");
			StringUtils.isValidObject(id, "参数错误");

			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");
			CommonResponseDto response = userApplicationService
					.changeUserAppIcon(userId, id, type, false);
			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/listUserApp")
	public void listUserApp(Model model) {
		try {

			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			ListDto<UserApplicationDto> response = userApplicationService
					.findUserApp(userId);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("apps", response.getData());
			}
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/deleteUserApp")
	public void deleteUserApp(Model model, @RequestBody JSONArray jsonArray) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");
			List<Integer> ids = new ArrayList<>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getInteger("id"));
			}
			CommonResponseDto response = userApplicationService
					.deleteApplication(userId, ids);
			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/userApp/changeAppsIcon", method = RequestMethod.POST)
	public void changeAppsIcon(Model model, @RequestBody JSONArray jsonArray) {
		// type==1代表增加到快速启动栏
		// type==2代表增加到开始菜单
		// type==4代表增加到桌面
		// type==3代表增加到快速启动栏和开始菜单
		// type==5代表增加到快速启动栏和桌面
		// type==6代表增加到开始菜单和桌面
		// type==7代表增加到快速启动栏和开始菜单和桌面
		try {

			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<UserAppIconDto> userApps = new ArrayList<>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); i++) {
				userApps.add(new UserAppIconDto(jsonArray.getJSONObject(i)
						.getInteger("id"), jsonArray.getJSONObject(i)
						.getInteger("type")));
			}

			CommonResponseDto response = userApplicationService
					.changeUserAppIcon(userId, userApps, true);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}
}
