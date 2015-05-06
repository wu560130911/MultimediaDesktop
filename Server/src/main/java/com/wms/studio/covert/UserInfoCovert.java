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

import com.wms.studio.api.dto.user.UserInfoDto;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("userInfoCovert")
public class UserInfoCovert extends AbstractCovert<User, UserInfoDto> {

	@Override
	public UserInfoDto covertToDto(User e) {

		if (e == null) {
			return null;
		}

		UserInfoDto userInfo = new UserInfoDto();
		userInfo.setCredit(e.getCredit());
		userInfo.setDisable(e.isDisable());
		userInfo.setEmail(e.getEmail());
		userInfo.setId(e.getId());
		userInfo.setLastLoginTime(e.getLastLoginTime());
		userInfo.setName(e.getName());
		userInfo.setRegisterTime(e.getRegisterTime());
		userInfo.setRole(e.getRole());
		userInfo.setvStatus(e.isvStatus());

		return userInfo;
	}

	@Override
	public User covertToEntity(UserInfoDto d) throws VerificationException {

		throw new VerificationException("不支持此方法");
	}

}
