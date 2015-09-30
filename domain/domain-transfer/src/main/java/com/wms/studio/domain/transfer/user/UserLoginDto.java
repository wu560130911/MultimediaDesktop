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
package com.wms.studio.domain.transfer.user;

import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class UserLoginDto extends CommonResponseDto implements Serializable {

	private String id;// 账号

	private String password;

	private boolean disable;// 是否被锁定

	private String salt;// 加密字符串

	private boolean vStatus;// 验证状态

	private String name;

	private UserRole role;

	private UserDisableReason disableReason;

	public UserLoginDto() {
	}

	public UserLoginDto(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isvStatus() {
		return vStatus;
	}

	public void setvStatus(boolean vStatus) {
		this.vStatus = vStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserRole getRole() {
		return role;
	}

	public UserDisableReason getDisableReason() {
		return disableReason;
	}

	public void setDisableReason(UserDisableReason disableReason) {
		this.disableReason = disableReason;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
