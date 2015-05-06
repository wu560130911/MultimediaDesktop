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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author WMS
 * @version 4.2
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tb_music")
public class Music implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;// 编号

	@Column(length = 100, nullable = false)
	private String title;// 标题

	@Column(length = 50, nullable = false)
	private String singer;// 歌手

	private int year;// 年份

	@Column(length = 20, nullable = false)
	private String type;// 类型

	private int duration;// 时长

	private long size;// 文件大小

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;// 上传用户

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;// 上传时间

	@Lob
	private String description;// 文件描述

	@Column(nullable = false, length = 300)
	private String filename;// 文件名称

	private Boolean auditingStatu;

	public Music(long id) {
		this.id = id;
	}

	public Music() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Boolean getAuditingStatu() {
		return auditingStatu;
	}

	public void setAuditingStatu(Boolean auditingStatu) {
		this.auditingStatu = auditingStatu;
	}

	@PrePersist
	private void beforePersist() {
		auditingStatu = Boolean.FALSE;
		time = new Date();
	}
}
