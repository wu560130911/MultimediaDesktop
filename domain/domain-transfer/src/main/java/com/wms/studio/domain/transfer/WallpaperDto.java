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
package com.wms.studio.domain.transfer;

import com.wms.studio.api.constant.WallpaperEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class WallpaperDto extends CommonResponseDto implements Serializable {

	private int id;

	private String path;

	private WallpaperEnum wallpaperType;

	private Date addDate;

	private boolean isDefault = false;

	private String userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public WallpaperEnum getWallpaperType() {
		return wallpaperType;
	}

	public void setWallpaperType(WallpaperEnum wallpaperType) {
		this.wallpaperType = wallpaperType;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public WallpaperDto() {
	}
	
	public WallpaperDto(int result) {
		super(result);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
