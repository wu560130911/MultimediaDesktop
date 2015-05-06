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
package com.wms.studio.api.dto.user;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wms.studio.api.dto.CommonResponseDto;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class UpdateUserDto extends CommonResponseDto implements Serializable {

	private String id;// 账号

	private String name;

	private String password;

	private String email;

	private String modifyPassword;

	public UpdateUserDto() {
	}

	public UpdateUserDto(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getModifyPassword() {
		return modifyPassword;
	}

	public void setModifyPassword(String modifyPassword) {
		this.modifyPassword = modifyPassword;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
