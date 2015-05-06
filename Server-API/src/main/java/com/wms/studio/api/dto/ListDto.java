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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class ListDto<T> extends CommonResponseDto {

	List<T> data;

	public void setData(List<T> data) {
		this.data = data;
	}
	
	public List<T> getData() {
		return data;
	}

	public ListDto() {
	}

	public ListDto(int result) {
		super(result);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
