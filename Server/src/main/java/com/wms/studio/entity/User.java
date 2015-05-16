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

package com.wms.studio.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.constant.UserRole;

/**
 * @author WMS
 * 
 */
@Entity
@Table(name = "tb_user")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SuppressWarnings("serial")
public class User implements Serializable {

	@Id
	@Column(length = UserConstant.USER_ID_MAX_LENGTH, nullable = false)
	private String id;// 账号

	@Column(length = UserConstant.USER_NAME_MAX_LENGTH, nullable = false)
	private String name;

	@Column(length = 80, nullable = false)
	private String password;

	@Column(length = 30, nullable = false)
	private String email;

	// 积分，这儿用来统计总的积分，但是积分的明细在积分表中
	@Column(length = 10, nullable = false)
	private long credit = 0;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;

	private boolean disable;// 是否被锁定

	private String salt;// 加密字符串

	private boolean vStatus;// 验证状态

	@Enumerated(EnumType.STRING)
	private UserRole role;// 用户身份

	@Enumerated(EnumType.STRING)
	private UserDisableReason disableReason;

	@ManyToOne
	@JoinColumn(name = "wallpaper_id")
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

	@PrePersist
	public void beforeRegisterUser() {
		registerTime = new Date();
		lastLoginTime = registerTime;
		disable = false;
		vStatus = false;
		if (role == null) {
			role = UserRole.用户;
		}
		disableReason = UserDisableReason.正常;
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
