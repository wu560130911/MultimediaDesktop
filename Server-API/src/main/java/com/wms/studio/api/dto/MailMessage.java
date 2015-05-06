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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wms.studio.api.dto.user.CheckEmailDto;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class MailMessage implements Serializable {

	private String header;

	private CheckEmailDto checkEmailDto;

	private Map<Object, Object> valuesMap = new HashMap<Object, Object>();

	public MailMessage() {
	}
	
	public MailMessage(String header){
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public CheckEmailDto getCheckEmailDto() {
		return checkEmailDto;
	}

	public void setCheckEmailDto(CheckEmailDto checkEmailDto) {
		this.checkEmailDto = checkEmailDto;
	}

	public void setValuesMap(Map<Object, Object> valuesMap) {
		this.valuesMap = valuesMap;
	}

	public Map<Object, Object> getValuesMap() {
		return valuesMap;
	}

	public Object put(Object key, Object value) {
		return this.valuesMap.put(key, value);
	}

	public Object remove(Object key) {
		return this.valuesMap.remove(key);
	}

	public Object get(Object key) {
		return this.valuesMap.get(key);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
