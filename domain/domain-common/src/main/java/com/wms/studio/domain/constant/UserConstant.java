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
package com.wms.studio.domain.constant;

/**
 * @author WMS
 *
 */
public class UserConstant {

	public static final int SUCCESS = 1;//操作成功
	
	public static final int ERROR = -1;//错误
	
	public static final int SYSTEM_ERROR = -2;//系统错误
	
	public static final int EMAIL_INVALID = 2;//邮箱已经存在，无效
	
	public static final int USER_ID_EXISTS = 3;//用户账号已经存在
	
	public static final int USER_ID_NOT_EXISTS = 4;//用户账号不存在
	
	public static final int USER_ID_MIN_LENGTH=4;//用户账号最小长度
	
	public static final int USER_ID_MAX_LENGTH=20;//用户账号最大长度
	
	public static final int USER_NAME_MIN_LENGTH=1;//用户姓名最小长度
	
	public static final int USER_NAME_MAX_LENGTH=10;//用户姓名最大长度
	
	public static final int USER_PASSWORD_MIN_LENGTH=6;
	
	public static final int USER_PASSWORD_MAX_LENGTH=20;
	
	public static final String HASH_ALGORITHM = "SHA-1";
	
	public static final int HASH_INTERATIONS = 1024;
	
	public static final int SALT_SIZE = 8;
	
	public static final String USER_EMAIL_VALID_PATH="/user/valid/";
	
	public static final String DEFAULT_ERROR_MESSAGE="系统异常,请稍后再试";
	
}
