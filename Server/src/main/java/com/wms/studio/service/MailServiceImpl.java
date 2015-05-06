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
package com.wms.studio.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.MailMessage;
import com.wms.studio.api.dto.user.CheckEmailDto;
import com.wms.studio.api.service.MailService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.security.utils.Cryptos;
import com.wms.studio.utils.Encodes;

/**
 * 
 * @author WMS
 * 
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Service("mailService")
public class MailServiceImpl implements MailService {

	private static final Logger log = Logger.getLogger(MailServiceImpl.class);

	@Autowired()
	@Qualifier("mailSender")
	private MailSender mailSender;

	@Value("#{props['system.webHost']}")
	private String webHost;

	@Value("#{props['system.mailSalt']}")
	private String mailSalt;

	public void setWebHost(String webHost) {
		this.webHost = webHost;
	}

	public void setMailSalt(String mailSalt) {
		this.mailSalt = mailSalt;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public CommonResponseDto sendSystemReport() {
		return null;
	}

	@Override
	public CommonResponseDto sendValidEmail(MailMessage mailMessage) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (mailMessage == null) {
				log.info("[邮件发送]-[参数校验]-[MailMessage参数不允许为空]");
				throw new VerificationException("MailMessage参数不允许为空");
			}

			if (StringUtils.isBlank(mailMessage.getHeader())) {
				log.info("[邮件发送]-[参数校验]-[邮件主题不允许为空]");
				throw new VerificationException("邮件主题不允许为空");
			}

			if (mailMessage.getCheckEmailDto() == null) {
				log.info("[邮件发送]-[参数校验]-[CheckEmailDto不允许为空]");
				throw new VerificationException("CheckEmailDto不允许为空");
			}

			StringBuffer urlBuffer = new StringBuffer();
			// 地址+邮箱验证地址+用户账号
			urlBuffer.append(webHost)
					.append(UserConstant.USER_EMAIL_VALID_PATH)
					.append(mailMessage.getCheckEmailDto().getUserId());

			String userJson = JSON.toJSONString(mailMessage.getCheckEmailDto());

			if (StringUtils.isBlank(userJson)) {
				log.fatal("[邮件发送]-[参数生成]-[生成用户json数据失败]");
				throw new VerificationException("生成用户json数据失败");
			}

			String encrypt = Encodes.encodeHex(Cryptos.aesEncrypt(
					userJson.getBytes(), Encodes.decodeHex(mailSalt)));

			if (StringUtils.isBlank(encrypt)) {
				log.fatal("[邮件发送]-[参数生成]-[加密数据失败]");
				throw new VerificationException("加密数据失败");
			}

			urlBuffer.append("?p=").append(encrypt);

			mailMessage.put("url", urlBuffer.toString());

			if (mailSender.sendMail(mailMessage)) {
				responseDto.setResult(UserConstant.SUCCESS);
			} else {
				log.fatal("[邮件发送]-[发送邮件失败]");
				throw new VerificationException("发送邮件失败");
			}

		} catch (Exception e) {
			if(e instanceof VerificationException){
				responseDto.setErrorMessage(e.getMessage());
			}else{
				responseDto.setErrorMessage(UserConstant.DEFAULT_ERROR_MESSAGE);
				log.fatal("[邮件发送]-[邮件发送异常]",e);
			}
		}

		return responseDto;
	}

	@Override
	public CommonResponseDto sendValidEmail(CheckEmailDto checkEmailDto) {
		return null;
	}

}
