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

import com.wms.studio.api.dto.user.LoginIpInfoDto;
import com.wms.studio.entity.LoginIp;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("loginIpConvert")
public final class LoginIpConvert extends AbstractCovert<LoginIp, LoginIpInfoDto> {

	@Override
	public LoginIpInfoDto covertToDto(LoginIp e) {

		if (e == null) {
			return null;
		}

		LoginIpInfoDto dto = new LoginIpInfoDto();

		dto.setIpaddress(e.getIpaddress());
		dto.setLoginTime(e.getLoginTime());
		dto.setLoginType(e.getLoginType());

		return dto;
	}

	@Override
	public LoginIp covertToEntity(LoginIpInfoDto d)
			throws VerificationException {
		throw new VerificationException("不支持此方法");
	}

}
