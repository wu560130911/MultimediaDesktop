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


import com.wms.studio.domain.constant.ApplicationType;
import com.wms.studio.domain.constant.UserRole;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WMS
 * @version 4.5
 */
@SuppressWarnings("serial")
public class Application implements Serializable {

	private Long id;

	private String name;

	private String iconCls;

	private String module;

	private String version;

	private String description;

	private String tip;

	private User addUser;

	private Date addDate;

	private UserRole role;// 标记是否为管理员应用

	// 本应用的类型
	private ApplicationType typeGroup;

	// 使用用户数
	private long useCount;

	// 这儿应该设置成至少三种状态的，但是因为原先设计原因，这儿不予改动
	private boolean auditingStatu;

	public Application() {
	}

	public Application(Long id) {
		this.id = id;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public User getAddUser() {
		return addUser;
	}

	public void setAddUser(User addUser) {
		this.addUser = addUser;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserRole getRole() {
		return role;
	}

	public ApplicationType getTypeGroup() {
		return typeGroup;
	}

	public void setTypeGroup(ApplicationType typeGroup) {
		this.typeGroup = typeGroup;
	}

	public long getUseCount() {
		return useCount;
	}

	public void setUseCount(long useCount) {
		this.useCount = useCount;
	}

	public void setAuditingStatu(boolean auditingStatu) {
		this.auditingStatu = auditingStatu;
	}

	public boolean isAuditingStatu() {
		return auditingStatu;
	}

}
