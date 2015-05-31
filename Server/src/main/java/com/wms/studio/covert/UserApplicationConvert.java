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
import com.wms.studio.api.dto.app.UserApplicationDto;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.UserApplication;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("userApplicationConvert")
public final class UserApplicationConvert extends
		AbstractCovert<UserApplication, UserApplicationDto> {

	@Resource
	@Qualifier("applicationCovert")
	private EntityConvertInterface<Application, ApplicationDto> applicationCovert;

	public void setApplicationCovert(
			EntityConvertInterface<Application, ApplicationDto> applicationCovert) {
		this.applicationCovert = applicationCovert;
	}

	@Override
	public UserApplicationDto covertToDto(UserApplication e) {

		if (e == null) {
			return null;
		}

		UserApplicationDto dto = new UserApplicationDto();
		dto.setApplication(applicationCovert.covertToDto(e.getApp()));
		dto.setAddDate(e.getAddDate());
		dto.setDesktopIcon(e.getDesktopIcon());
		dto.setId(e.getId());
		dto.setQuickStart(e.getQuickStart());
		dto.setScore(e.getScore());
		dto.setStartMenu(e.getStartMenu());
		dto.setUninstall(e.getUninstall());
		return dto;
	}

	@Override
	public UserApplication covertToEntity(UserApplicationDto d)
			throws VerificationException {
		throw new VerificationException("不支持此方法");
	}

}
