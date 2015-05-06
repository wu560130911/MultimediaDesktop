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
package com.wms.studio.api.dto.app;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserRole;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class ApplicationDto implements Serializable{

	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String iconCls;

	@NotNull
	private String module;

	@NotNull
	private String version;

	@NotNull
	private String description;

	@NotNull
	private String tip;

	private String userId;

	private Date addDate;

	@NotNull
	private UserRole role;// 标记是否为管理员应用

	@NotNull
	private ApplicationType typeGroup;

	private long useCount;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
