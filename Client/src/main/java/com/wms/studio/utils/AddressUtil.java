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

import javax.servlet.http.HttpServletRequest;

/**
 * @author WMS
 * 
 */
public class AddressUtil {

	private static final String DIR_SPILT = "/";

	//如果有多个代理，将会获取到用户使用的那个代理地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "本地";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	public static String formatPath(String root, String path) {
		StringBuffer resultPath = new StringBuffer();
		if (!root.startsWith(DIR_SPILT)) {
			resultPath.append(DIR_SPILT);
		}
		resultPath.append(root);
		if (!root.endsWith(DIR_SPILT) && !path.startsWith(DIR_SPILT)) {
			resultPath.append(DIR_SPILT);
		}
		return resultPath.append(path).toString();
	}
}
