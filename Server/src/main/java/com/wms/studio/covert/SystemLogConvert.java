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

import com.wms.studio.api.dto.SystemLogDto;
import com.wms.studio.entity.SystemLog;
import com.wms.studio.exception.VerificationException;

@Component("systemLogConvert")
public final class SystemLogConvert extends AbstractCovert<SystemLog, SystemLogDto> {

	@Override
	public SystemLogDto covertToDto(SystemLog e) {

		if (e == null) {
			return null;
		}
		SystemLogDto dto = new SystemLogDto();
		dto.setClassPath(e.getClassPath());
		dto.setId(e.getId());
		dto.setLever(e.getLever());
		dto.setLogDateTime(e.getLogDateTime());
		dto.setLogFrom(e.getLogFrom());
		dto.setMessage(e.getMessage());
		dto.setThreadName(e.getThreadName());

		return dto;
	}

	@Override
	public SystemLog covertToEntity(SystemLogDto d)
			throws VerificationException {
		throw new VerificationException("不支持此方法");
	}

}
