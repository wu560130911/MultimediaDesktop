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
package com.wms.studio.api.dto.page;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class PageSize implements Serializable{

	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_LIMIT = 15;

	Integer page;

	Integer limit;

	public PageSize() {
		this.page = DEFAULT_PAGE;
		this.limit = DEFAULT_LIMIT;
	}

	public PageSize(Integer page, Integer limit) {
		this.page = checkValue(page, DEFAULT_PAGE);
		this.limit = checkValue(limit, DEFAULT_LIMIT);
	}

	private Integer checkValue(Integer value, Integer defaultValue) {
		if (value == null || value < 1) {
			return defaultValue;
		}
		return value;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = checkValue(page, DEFAULT_PAGE);
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = checkValue(limit, DEFAULT_LIMIT);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
