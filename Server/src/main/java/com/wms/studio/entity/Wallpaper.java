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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wms.studio.api.constant.WallpaperEnum;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tb_wallpaper")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wallpaper implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(length = 250)
	private String path;

	@Enumerated(EnumType.STRING)
	private WallpaperEnum wallpaperType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date addDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private boolean isDefault = false;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@PrePersist
	private void beforPersist() {
		this.addDate = new Date();
	}
}
