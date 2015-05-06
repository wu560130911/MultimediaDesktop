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

package com.wms.studio.service.handler.user;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.MailMessage;
import com.wms.studio.api.dto.user.UserDto;
import com.wms.studio.api.service.MailService;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;
import com.wms.studio.service.user.UserMailHandler;
import com.wms.studio.service.user.UserMailHandlerFactoryBean;

/**
 * @author WMS
 * 
 */
@Service("registerUserSendEmailHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(beanName = "registerUserSendEmailHandler", handlerName = "registerUserHandler", scope = HandlerScope.After)
public class RegisterUserSendEmailHandler extends AbstractHandler {

	private static final Logger log = Logger
			.getLogger(RegisterUserSendEmailHandler.class);

	@Resource
	private UserRepository userRepository;

	@Resource
	private UserMailHandlerFactoryBean handler;

	@Resource
	private MailService mailService;

	@Value("#{props['system.registerSendValidMail']}")
	private Boolean isSendMail;

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setHandler(UserMailHandlerFactoryBean handler) {
		this.handler = handler;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setIsSendMail(Boolean isSendMail) {
		this.isSendMail = isSendMail;
	}

	@Override
	public boolean beforeHandler(HandlerData data) {

		UserDto userDto = getArgsObject(data, 0);

		CommonResponseDto response = getResult(data);

		if (userDto == null || response == null
				|| response.getResult() != UserConstant.SUCCESS) {
			return true;
		}

		return false;
	}

	@Override
	public boolean handler(HandlerData data) {

		UserDto userDto = getArgsObject(data, 0);

		User user = userRepository.findOne(userDto.getId());

		if (user == null) {
			return true;
		}

		// 不需要发送验证邮件
		if (isSendMail == null || isSendMail.equals(Boolean.FALSE)) {
			user.setvStatus(Boolean.TRUE);
			return true;
		}

		try {
			UserMailHandler mailHandler = handler
					.getHandler(EmailConstant.EMAIL_USER_REGISTER);
			MailMessage mailMessage = mailHandler.handlerMailMessage(
					user.getEmail(), user);

			CommonResponseDto mailResponseDto = mailService
					.sendValidEmail(mailMessage);

			if (UserConstant.SUCCESS != mailResponseDto.getResult()) {
				throw new VerificationException(
						mailResponseDto.getErrorMessage());
			}
		} catch (VerificationException e) {
			log.fatal("发送注册邮件绑定邮件失败，请检查", e);
		}
		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {

		return true;
	}

}
