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
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.ApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class ApplicationManagerController {

	private static final Logger log = Logger
			.getLogger(ApplicationManagerController.class);

	@Resource
	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@RequestMapping(value = "/admin/app/getUnCheckApp")
	public void listUnCheckApplication(Model model, Integer page,
			Integer limit, String userId, String name,
			ApplicationType typeGroup, UserRole role,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		ArrayList<UserRole> roles = new ArrayList<>(1);

		if (role != null) {
			roles.add(role);
		}

		PageDto<ApplicationDto> apps = applicationService.findBy(userId, name,
				startDate, endDate, typeGroup, roles.isEmpty() ? null : roles,
				Boolean.FALSE, new PageSize(page, limit), null);

		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}
	
	@RequestMapping(value = "/admin/app/getCheckApp")
	public void listCheckApplication(Model model, Integer page,
			Integer limit, String userId, String name,
			ApplicationType typeGroup, UserRole role,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		ArrayList<UserRole> roles = new ArrayList<>(1);

		if (role != null) {
			roles.add(role);
		}

		PageDto<ApplicationDto> apps = applicationService.findBy(userId, name,
				startDate, endDate, typeGroup, roles.isEmpty() ? null : roles,
				Boolean.TRUE, new PageSize(page, limit), null);

		model.addAttribute("apps", apps.getValues());
		model.addAttribute("total", apps.getTotalElements());
	}

	@RequestMapping(value = "/admin/app/checkApp")
	public void checkApplication(Model model, @RequestBody JSONArray jsonArray) {

		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<Long> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getLong("id"));
			}
			
			log.debug("管理员["+userId+"]正在审核应用.");
			CommonResponseDto response = applicationService.checkApplication(
					userId, ids);
			
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
