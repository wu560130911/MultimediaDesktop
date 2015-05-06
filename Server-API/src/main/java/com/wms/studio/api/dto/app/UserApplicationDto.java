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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class UserApplicationDto implements Serializable {

	private Integer id;

	private ApplicationDto application;

	private Date addDate;

	private Boolean quickStart;

	private Boolean desktopIcon;

	private Boolean startMenu;

	private Boolean uninstall;

	// 用户评分
	private double score;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplicationDto getApplication() {
		return application;
	}

	public void setApplication(ApplicationDto application) {
		this.application = application;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Boolean getQuickStart() {
		return quickStart;
	}

	public void setQuickStart(Boolean quickStart) {
		this.quickStart = quickStart;
	}

	public Boolean getDesktopIcon() {
		return desktopIcon;
	}

	public void setDesktopIcon(Boolean desktopIcon) {
		this.desktopIcon = desktopIcon;
	}

	public Boolean getStartMenu() {
		return startMenu;
	}

	public void setStartMenu(Boolean startMenu) {
		this.startMenu = startMenu;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Boolean getUninstall() {
		return uninstall;
	}

	public void setUninstall(Boolean uninstall) {
		this.uninstall = uninstall;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
