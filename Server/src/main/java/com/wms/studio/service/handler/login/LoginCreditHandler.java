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
package com.wms.studio.service.handler.login;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.LoginType;
import com.wms.studio.api.dto.user.LoginIpDto;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.entity.Credit;
import com.wms.studio.entity.User;
import com.wms.studio.repository.CreditRepository;
import com.wms.studio.repository.LoginIpRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

@Service("loginCreditHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(handlerName = "loginHandler", scope = HandlerScope.After, beanName = "loginCreditHandler")
public class LoginCreditHandler extends AbstractHandler {

	private static final Logger log = Logger
			.getLogger(LoginCreditHandler.class);

	@Autowired
	private LoginIpRepository loginIpRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CreditRepository creditRepository;

	@Value("#{credits['credit.add.login']}")
	private int loginCredit;

	public void setLoginCredit(int loginCredit) {
		this.loginCredit = loginCredit;
	}

	public void setLoginIpRepository(LoginIpRepository loginIpRepository) {
		this.loginIpRepository = loginIpRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setCreditRepository(CreditRepository creditRepository) {
		this.creditRepository = creditRepository;
	}

	@Override
	public boolean beforeHandler(HandlerData data) {

		log.info("[Handler]-[LoginCreditHandler]-首次登录送积分处理逻辑");

		Object[] objects = data.getValue("args");

		if (objects == null || objects.length == 0) {
			return true;
		}

		if (loginCredit <= 0) {
			log.info("[Handler]-[LoginCreditHandler]-管理员将首次登录送积分关闭");
			return true;
		}

		LoginIpDto loginIp = (LoginIpDto) objects[0];

		if (loginIp == null || StringUtils.isBlank(loginIp.getUserId())) {
			return true;
		}

		if (LoginType.登录成功.equals(loginIp.getLoginType())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean handler(HandlerData data) {
		Object[] objects = data.getValue("args");

		if (objects == null || objects.length == 0) {
			return true;
		}

		LoginIpDto loginIp = (LoginIpDto) objects[0];
		DateTime nowTime = new DateTime();
		DateTime startOfDay = nowTime.withTimeAtStartOfDay();
		Long loginCount = loginIpRepository
				.countByLoginTimeAfterAndLoginTypeAndUser(startOfDay.toDate(),
						LoginType.登录成功, new User(loginIp.getUserId()));
		if (loginCount == null || (long) loginCount <= 1L) {

			Credit credit = new Credit(new User(loginIp.getUserId()),
					com.wms.studio.api.constant.CreditType.收入);
			credit.setCreditNum(loginCredit);
			credit.setDescriptions("首次登录奖励");
			userRepository.updateUserCreadit(credit.getCreditNum(),
					loginIp.getUserId());
			creditRepository.save(credit);
		}

		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {
		return true;
	}

}
