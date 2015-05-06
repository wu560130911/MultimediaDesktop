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

package com.wms.studio.constant;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author WMS
 * 
 */
public class Constant {

	public static final String USER_STATUS_KEY = "user_status";

	public static final String USER_NORMAL_STATUS = "1";// 正常状态

	public static final String USER_FORACE_STATUS = "2";// 强制下线状态

	public static final String USER_KICKOUT_STATUS = "3";// 超过最大登入次数限制状态
	
	public static final String USER_REGISTER_STATUS = "4";// 用户已经注册成功但是还有进行邮箱验证
	
	public static final String USER_VALID_EMAIL_SUCCESS = "5";
	
	public static final String USER_VALID_EMAIL_FAIL = "6";
	
	public static final String USER_FIND_PASSWORD_SUCCESS = "7";
	
	public static final String USER_FIND_PASSWORD_FAIL = "8";
	
	public static final String EMAIL_USER_CHANGE_EMAIL_SUCCESS = "9";
	
	public static final String USER_FIND_PASSWORD_ERROR = "10";

	public static final String SPRING_WEB_MVC_APPLICATION_KEY = DispatcherServlet.SERVLET_CONTEXT_PREFIX
			+ "springServlet";

	public static final String SPRING_WEB_APPLICATION_CONTEXT_KEY = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
}
