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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 本类已经不使用，使用spring进行注入
 * @author WMS
 * 
 */
public class SystemProps {

	public static final String SERVER_FILE = "server.properties";

	public static Properties props = null;

	public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
	
	public static boolean InitProps() {
		try {
			props = PropertiesLoaderUtils.loadAllProperties(SERVER_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return props != null;
	}

	public static String getValue(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static String getValue(String key) {
		return getValue(key, null);
	}
	
	public static String getDateFormatString(long times){
		return sdf.format(times);
	}
	
}
