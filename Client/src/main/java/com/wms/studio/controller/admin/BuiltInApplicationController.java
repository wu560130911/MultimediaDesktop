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
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.app.BuiltInApplicationDto;
import com.wms.studio.api.service.BuiltInApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 *
 */
@Controller
public class BuiltInApplicationController {

	private static final Logger log = Logger.getLogger(BuiltInApplicationController.class);
	
	@Resource
	private BuiltInApplicationService builtInApplicationService;
	
	public void setBuiltInApplicationService(
			BuiltInApplicationService builtInApplicationService) {
		this.builtInApplicationService = builtInApplicationService;
	}
	
	@RequestMapping("/admin/builtInApp/add")
	public void addBuiltInApplication(Model model,@RequestBody JSONArray jsonArray){
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<BuiltInApplicationDto> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				BuiltInApplicationDto dto = new BuiltInApplicationDto();
				dto.setApplicationId(jsonArray.getJSONObject(i).getLong("id"));
				dto.setUninstall(Boolean.TRUE);
				ids.add(dto);
			}
			
			log.debug("管理员["+userId+"]正在添加内置应用.");
			CommonResponseDto response = builtInApplicationService.addBuiltInApplication(userId, ids);
			
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
