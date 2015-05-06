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

package com.wms.studio.service.user;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Service("userMailHandler")
public class UserMailHandlerFactoryBean {

	@Resource
	@Qualifier("registerMailHandler")
	private UserMailHandler registerMailHandler;

	@Resource
	@Qualifier("findPasswordHandler")
	private UserMailHandler findPasswordHandler;

	@Resource
	@Qualifier("changeEmailHandler")
	private UserMailHandler changeEmailHandler;

	public void setRegisterMailHandler(UserMailHandler registerMailHandler) {
		this.registerMailHandler = registerMailHandler;
	}

	public void setFindPasswordHandler(UserMailHandler findPasswordHandler) {
		this.findPasswordHandler = findPasswordHandler;
	}

	public void setChangeEmailHandler(UserMailHandler changeEmailHandler) {
		this.changeEmailHandler = changeEmailHandler;
	}

	public UserMailHandler getHandler(String handler)
			throws VerificationException {

		if (EmailConstant.EMAIL_USER_CHANGE_EMAIL.equals(handler)) {
			return changeEmailHandler;
		} else if (EmailConstant.EMAIL_USER_FIND_PASSWORD.equals(handler)) {
			return findPasswordHandler;
		} else if (EmailConstant.EMAIL_USER_REGISTER.equals(handler)) {
			return registerMailHandler;
		} else {
			throw new VerificationException("系统不能设别的邮件类型");
		}

	}
}
