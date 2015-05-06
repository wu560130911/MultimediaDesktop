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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserRole;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class UserAppDto implements Serializable {

	private Integer id;

	private String name;

	private String iconCls;

	private String module;

	private String version;

	private String tip;

	private UserRole role;// 标记是否为管理员应用

	// 本应用的类型
	private ApplicationType typeGroup;

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

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
