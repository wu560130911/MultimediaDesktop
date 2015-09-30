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

import java.io.Serializable;
import java.util.Date;

/**
 * @author WMS
 * @version 3.5
 */
@SuppressWarnings("serial")
public class Notebook implements Serializable {

	private long id;// 编号
	
	private User user;// 用户
	
	private String description;// 内容
	
	private String type;// 分类 生活日记、工作日记、观察日记，学习日记
	
	private String weather;// 天气
	
	private String mood;// 心情

	private Date notedate;

	private Date writetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public Date getNotedate() {
		return notedate;
	}

	public void setNotedate(Date notedate) {
		this.notedate = notedate;
	}

	public Date getWritetime() {
		return writetime;
	}

	public void setWritetime(Date writetime) {
		this.writetime = writetime;
	}

}
