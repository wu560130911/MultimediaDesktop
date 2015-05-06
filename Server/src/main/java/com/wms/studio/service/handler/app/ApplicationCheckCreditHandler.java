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

package com.wms.studio.service.handler.app;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.Credit;
import com.wms.studio.repository.ApplicationRepository;
import com.wms.studio.repository.CreditRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
@Service("applicationCheckCreditHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(handlerName = "checkApplicationHandler", scope = HandlerScope.After, beanName = "applicationCheckCreditHandler")
public class ApplicationCheckCreditHandler extends AbstractHandler {

	private static final Logger log = Logger
			.getLogger(ApplicationCheckCreditHandler.class);

	@Resource
	private ApplicationRepository applicationRepository;

	@Resource
	private UserRepository userRepository;

	@Resource
	private CreditRepository creditRepository;

	@Value("#{credits['credit.add.checkApplication']}")
	private Integer checkApplicationCredit;

	public void setCheckApplicationCredit(Integer checkApplicationCredit) {
		this.checkApplicationCredit = checkApplicationCredit;
	}

	public void setApplicationRepository(
			ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setCreditRepository(CreditRepository creditRepository) {
		this.creditRepository = creditRepository;
	}

	@Override
	public boolean beforeHandler(HandlerData data) {
		log.info("[Handler]-[ApplicationCheckCreditHandler]-应用通过审核奖励积分");

		if (checkApplicationCredit == null || checkApplicationCredit <= 0) {
			log.info("[Handler]-[ApplicationCheckCreditHandler]-本处理处于关闭状态");
			return true;
		}

		CommonResponseDto response = getResult(data);

		List<Long> ids = getArgsObject(data, 1);

		if (response == null || UserConstant.SUCCESS != response.getResult()
				|| ids == null || ids.isEmpty()) {
			return true;
		}

		return false;
	}

	@Override
	public boolean handler(HandlerData data) {

		List<Long> ids = getArgsObject(data, 1);

		for (Long id : ids) {
			Application application = applicationRepository.findOne(id);
			if (application != null && application.isAuditingStatu()) {
				Credit credit = new Credit(application.getAddUser(),
						com.wms.studio.api.constant.CreditType.收入);
				credit.setCreditNum(checkApplicationCredit);
				credit.setDescriptions("[" + application.getName()
						+ "]通过管理员审核奖励积分");
				userRepository.updateUserCreadit(credit.getCreditNum(),
						application.getAddUser().getId());
				creditRepository.save(credit);
			}
		}

		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {

		return false;
	}

}
