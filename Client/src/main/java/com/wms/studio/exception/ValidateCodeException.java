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

package com.wms.studio.exception;

import org.apache.shiro.authc.AccountException;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class ValidateCodeException extends AccountException {

	public ValidateCodeException() {
		super();
	}

	public ValidateCodeException(String message) {
		super(message);
	}

	public ValidateCodeException(Throwable cause) {
		super(cause);
	}

	public ValidateCodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
