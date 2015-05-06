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
package com.wms.studio.covert;

import org.springframework.stereotype.Component;

import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.User;

/**
 * 
 * @author WMS
 * 
 */
@Component("applicationCovert")
public class ApplicationCovert extends
		AbstractCovert<Application, ApplicationDto> {

	@Override
	public ApplicationDto covertToDto(Application e) {

		if (e == null) {
			return null;
		}

		ApplicationDto dto = new ApplicationDto();

		dto.setAddDate(e.getAddDate());
		dto.setDescription(e.getDescription());
		dto.setIconCls(e.getIconCls());
		dto.setId(e.getId());
		dto.setModule(e.getModule());
		dto.setName(e.getName());
		dto.setRole(e.getRole());
		dto.setTip(e.getTip());
		dto.setTypeGroup(e.getTypeGroup());
		dto.setUseCount(e.getUseCount());
		dto.setVersion(e.getVersion());
		dto.setUserId(e.getAddUser().getId());

		return dto;
	}

	@Override
	public Application covertToEntity(ApplicationDto d) {

		if (d == null) {
			return null;
		}

		Application app = new Application();

		app.setAddDate(d.getAddDate());
		app.setDescription(d.getDescription());
		app.setIconCls(d.getIconCls());
		app.setId(d.getId());
		app.setModule(d.getModule());
		app.setName(d.getName());
		app.setRole(d.getRole());
		app.setTip(d.getTip());
		app.setTypeGroup(d.getTypeGroup());
		app.setUseCount(d.getUseCount());
		app.setVersion(d.getVersion());
		app.setAddUser(new User(d.getUserId()));
		return app;
	}

}
