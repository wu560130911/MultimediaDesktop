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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wms.studio.api.dto.ComboboxStoreDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.SystemLogDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.SystemLogService;

/**
 * @author WMS
 * 
 */
@Controller
public class SystemLogController {

	@Resource
	private SystemLogService systemLogService;

	public void setSystemLogService(SystemLogService systemLogService) {
		this.systemLogService = systemLogService;
	}

	@RequestMapping("admin/systemLog/getLogServer")
	public void listLogServer(Model model) {
		List<String> servers = systemLogService.getLogServer();
		List<ComboboxStoreDto> dtos = new ArrayList<>();
		for (String server : servers) {
			dtos.add(new ComboboxStoreDto(server));
		}
		model.addAttribute("servers", dtos);
	}

	@RequestMapping("admin/systemLog/list")
	public void listLogs(Model model, PageSize pageSize, String logFrom,
			String classPath, String threadName, String lever, String message,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {

		PageDto<SystemLogDto> logs = systemLogService.findBy(logFrom,
				classPath, message, threadName, lever, startDate, endDate,
				pageSize, null);

		model.addAttribute("logs", logs.getValues());
		model.addAttribute("total", logs.getTotalElements());
	}
}
