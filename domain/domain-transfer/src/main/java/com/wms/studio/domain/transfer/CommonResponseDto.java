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

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class CommonResponseDto implements Serializable{

	private int result;

	private String errorMessage;

	public CommonResponseDto() {
	}

	public CommonResponseDto(int result) {
		this.result = result;
	}

	public CommonResponseDto(int result, String errorMessage) {
		this.result = result;
		this.errorMessage = errorMessage;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
