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
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class UserDto extends CommonResponseDto implements Serializable {

	private String id;// 账号

	private String name;

	private String password;

	private String email;

	private long credit = 0;

	private Date registerTime;

	private Date lastLoginTime;

	private boolean disable;// 是否被锁定

	private String salt;// 加密字符串

	private boolean vStatus = false;// 验证状态

	private UserRole role = UserRole.用户;

	private String registerAddress;

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

	public long getCredit() {
		return credit;
	}

	public void setCredit(long credit) {
		this.credit = credit;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public UserDto() {
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
