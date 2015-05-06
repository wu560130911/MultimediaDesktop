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
package com.wms.studio.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WMS 这儿本应该设置成初始化后就不允许修改里面的内容的，但是为了rpc传输，所以这儿必须要是可序列化的
 */
@SuppressWarnings("serial")
public class PageDto<T> implements Serializable {

	private List<T> values;

	private long totalPage;

	private long totalElements;

	public PageDto() {
	}

	public PageDto(List<T> values) {
		this.values = values;
	}

	public PageDto(final List<T> values, final long totalPage,
			final long totalElements) {
		this.values = values;
		this.totalElements = totalElements;
		this.totalPage = totalPage;
	}

	public List<T> getValues() {
		return values;
	}

	public void setValues(List<T> values) {
		this.values = values;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PageDto<T> EMPTY_PAGE(){
		return new PageDto<T>(Collections.EMPTY_LIST, 0, 0);
	}
}
