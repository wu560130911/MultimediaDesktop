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

import com.wms.studio.domain.constant.CreditType;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分明细，包含所有的积分获得和使用情况
 * 
 * @author WMS
 */
public class Credit implements Serializable {

	private Long id;

	private Date changeTime;

	private int creditNum;

	private String descriptions;

	private User user;

	private CreditType creditType;

	public Credit() {
	}

	public Credit(User user) {
		this.user = user;
	}

	public Credit(User user, CreditType creditType) {
		this.user = user;
		this.creditType = creditType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public int getCreditNum() {
		return creditNum;
	}

	public void setCreditNum(int creditNum) {
		this.creditNum = creditNum;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CreditType getCreditType() {
		return creditType;
	}

	public void setCreditType(CreditType creditType) {
		this.creditType = creditType;
	}
}
