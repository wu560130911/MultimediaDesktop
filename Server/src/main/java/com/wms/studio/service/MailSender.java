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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.api.dto.MailMessage;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author WMS
 * 
 */
@Service("mailSender")
public class MailSender {

	@Autowired
	private JavaMailSenderImpl senderMail;

	private static final Logger log = Logger.getLogger(MailSender.class);

	private static Configuration cfg = new Configuration();

	private static final String FREEMAKER_FILE = ".ftl";

	static {
		try {
			cfg.setDirectoryForTemplateLoading(new File(MailSender.class
					.getResource("/ftl").getFile()));
			cfg.setDefaultEncoding("UTF-8");
			JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
		} catch (IOException e) {
			log.fatal("初始化FreeMaker配置异常", e);
		}
	}

	public void setSenderMail(JavaMailSenderImpl senderMail) {
		this.senderMail = senderMail;
	}

	public boolean sendMail(MailMessage mailMessage) {

		MimeMessage mimeMessage = senderMail.createMimeMessage();
		// multipart模式 为true时发送附件 可以设置html格式
		MimeMessageHelper messageHelper;

		try {
			messageHelper = new MimeMessageHelper(mimeMessage, true,
					EmailConstant.DEFAULT_CHARSET);
			messageHelper.setFrom(senderMail.getUsername(), senderMail
					.getJavaMailProperties().getProperty("mail.fromName"));
			messageHelper.addTo(mailMessage.getCheckEmailDto().getEmailAddress());
			messageHelper.setSubject(mailMessage.getHeader());
			messageHelper.setSentDate(new Date());

			Template template = cfg.getTemplate(mailMessage.getCheckEmailDto()
					.getType() + FREEMAKER_FILE);

			String text = processMailText(template, mailMessage);
			if (StringUtils.isBlank(text)) {
				throw new VerificationException("模板处理异常");
			}

			messageHelper.setText(text, true);
			senderMail.send(mimeMessage);

			return true;
		} catch (Exception e) {
			log.fatal("[发送邮件]-[发送邮件异常]", e);
		}
		return false;
	}

	private static String processMailText(Template template,
			MailMessage mailMessage) {
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			template.process(mailMessage, writer);
			return writer.toString();
		} catch (TemplateException | IOException e) {
			log.fatal("处理邮件模板异常", e);
		}
		return null;
	}

}
