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

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.MailMessage;
import com.wms.studio.api.dto.user.CheckEmailDto;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;

/**
 * 
 * @author WMS
 * 
 */
@Service("changeEmailHandler")
public class ChangeEmailHandler implements UserMailHandler {

	@Value("#{props['system.validMailDate']}")
	private int validMailDate;

	public void setValidMailDate(int validMailDate) {
		this.validMailDate = validMailDate;
	}

	@Override
	public MailMessage handlerMailMessage(String email, User user) {

		CheckEmailDto dto = new CheckEmailDto();
		dto.setType(EmailConstant.EMAIL_USER_CHANGE_EMAIL);// 邮件类型
		dto.setEmailAddress(email);// 邮件地址

		DateTime dateTime = new DateTime();
		dto.setNowTime(dateTime.toDate());// 当前时间
		dateTime = dateTime.plusHours(validMailDate);
		dto.setLastTime(dateTime.toDate());// 链接过期时间
		dto.setUserId(user.getId());// 用户账号

		MailMessage mailMessage = new MailMessage(
				EmailConstant.EMAIL_USER_CHGEMAIL_TITLE);
		mailMessage.put("findDate", dto.getNowTime());
		mailMessage.put("userName", user.getName());
		mailMessage.put("validTime", validMailDate);
		mailMessage.setCheckEmailDto(dto);

		return mailMessage;
	}

	@Override
	public CommonResponseDto handlerMailResult(User user, CheckEmailDto dto)
			throws VerificationException {

		user.setEmail(dto.getEmailAddress());
		
		return null;
	}

}
