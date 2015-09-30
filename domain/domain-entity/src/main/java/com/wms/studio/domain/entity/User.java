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

package com.wms.studio.domain.entity;


import com.wms.studio.domain.constant.UserDisableReason;
import com.wms.studio.domain.constant.UserRole;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private String id;// 账号

	private String name;

	private String password;

	private String email;

	// 积分，这儿用来统计总的积分，但是积分的明细在积分表中
	private long credit = 0;

	private Date registerTime;

	private Date lastLoginTime;

	private boolean disable;// 是否被锁定

	private String salt;// 加密字符串

	private boolean vStatus;// 验证状态

	private UserRole role;// 用户身份

	private UserDisableReason disableReason;

	private Wallpaper wallpaper;

	public User() {
	}

	public User(String id) {
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

	public UserDisableReason getDisableReason() {
		return disableReason;
	}

	public void setDisableReason(UserDisableReason disableReason) {
		this.disableReason = disableReason;
	}

	public Wallpaper getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(Wallpaper wallpaper) {
		this.wallpaper = wallpaper;
	}

}
