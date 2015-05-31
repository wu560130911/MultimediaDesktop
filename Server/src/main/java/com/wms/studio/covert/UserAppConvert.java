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

import com.wms.studio.api.dto.app.UserAppDto;
import com.wms.studio.entity.UserApplication;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("userAppConvert")
public final class UserAppConvert extends AbstractCovert<UserApplication, UserAppDto> {

	@Override
	public UserAppDto covertToDto(UserApplication e) {

		if (e == null) {
			return null;
		}
		UserAppDto dto = new UserAppDto();

		dto.setIconCls(e.getApp().getIconCls());
		dto.setId(e.getId());
		dto.setModule(e.getApp().getModule());
		dto.setName(e.getApp().getName());
		dto.setRole(e.getApp().getRole());
		dto.setTip(e.getApp().getTip());
		dto.setTypeGroup(e.getApp().getTypeGroup());
		dto.setVersion(e.getApp().getVersion());

		return dto;
	}

	@Override
	public UserApplication covertToEntity(UserAppDto d)
			throws VerificationException {

		throw new VerificationException("不支持此方法");
	}

}
