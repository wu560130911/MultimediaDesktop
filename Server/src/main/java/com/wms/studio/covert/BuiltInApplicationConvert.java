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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.api.dto.app.BuiltInApplicationDto;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.BuiltInApplication;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("builtInApplicationConvert")
public final class BuiltInApplicationConvert extends
		AbstractCovert<BuiltInApplication, BuiltInApplicationDto> {

	@Resource
	@Qualifier("applicationCovert")
	private EntityConvertInterface<Application, ApplicationDto> applicationCovert;

	@Override
	public BuiltInApplicationDto covertToDto(BuiltInApplication e) {

		if (e == null) {
			return null;
		}

		BuiltInApplicationDto dto = new BuiltInApplicationDto();
		dto.setAddDate(e.getAddDate());
		dto.setApplication(applicationCovert.covertToDto(e.getApplication()));
		dto.setUninstall(e.getUninstall());
		dto.setId(e.getId());

		return dto;
	}

	@Override
	public BuiltInApplication covertToEntity(BuiltInApplicationDto d)
			throws VerificationException {
		throw new VerificationException("不支持此方法");
	}

}
