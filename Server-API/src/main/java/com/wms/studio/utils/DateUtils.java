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
package com.wms.studio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WMS
 * 
 */
public class DateUtils {

	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String YYYMMDDHHIISS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHIISSMS = "yyyyMMddHHmmssms";

	public static String getDateFormat(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
