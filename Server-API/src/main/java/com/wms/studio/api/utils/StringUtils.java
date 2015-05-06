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
package com.wms.studio.api.utils;

import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	public static final boolean isLengthValid(String value, int minLength,
			int maxLength) {
		if (isBlank(value)) {
			return false;
		}
		return value.length() > minLength && value.length() < maxLength;
	}

	public static boolean checkEmail(String email) {
		if (isBlank(email)) {
			return false;
		}
		return email.matches(EMAIL_REGEX);
	}

	public static void isValidObject(Object value, String errorMessage)
			throws VerificationException {
		if (value == null) {
			throw new VerificationException(errorMessage);
		}
	}
	
	public static void isValidString(String value, String errorMessage)
			throws VerificationException {
		if (isBlank(value)) {
			throw new VerificationException(errorMessage);
		}
	}

	public static void isValidString(String value, String errorMessage,
			int minLength, int maxLength) throws VerificationException {
		if (!isLengthValid(value, minLength, maxLength)) {
			throw new VerificationException(errorMessage);
		}
	}
}
