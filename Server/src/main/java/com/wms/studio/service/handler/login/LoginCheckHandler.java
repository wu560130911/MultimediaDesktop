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

import org.apache.commons.lang3.builder.ToStringBuilder;
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
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.dto.user.LoginIpDto;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.entity.User;
import com.wms.studio.repository.LoginIpRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

@Service("loginCheckHandler")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Handler(handlerName = "loginHandler", scope = HandlerScope.After, beanName = "loginCheckHandler")
public class LoginCheckHandler extends AbstractHandler {

	private static final Logger log = Logger.getLogger(LoginCheckHandler.class);

	@Autowired
	private LoginIpRepository loginIpRepository;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@Value("#{props['system.login.failDisable']}")
	private int failDisableCount;

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setLoginIpRepository(LoginIpRepository loginIpRepository) {
		this.loginIpRepository = loginIpRepository;
	}

	public void setFailDisableCount(int failDisableCount) {
		this.failDisableCount = failDisableCount;
	}

	@Override
	public boolean beforeHandler(HandlerData data) {

		log.info("登录失败次数校验处理:" + ToStringBuilder.reflectionToString(data));

		Object[] objects = data.getValue("args");

		if (objects == null || objects.length == 0) {
			return true;
		}

		LoginIpDto loginIp = (LoginIpDto) objects[0];

		// 查询今天登录失败的次数
		if (loginIp == null || StringUtils.isBlank(loginIp.getUserId())) {
			return true;
		}

		if (loginIp.getLoginType() == LoginType.登录成功) {
			return true;
		}

		user = userRepository.findOne(loginIp.getUserId());

		// 如果用户不存在、已经锁定或者未验证则不进行任何处理
		if (user == null || user.isDisable() || !user.isvStatus()) {
			return true;
		}

		return false;
	}

	@Override
	public boolean handler(HandlerData data) {

		Object[] objects = data.getValue("args");

		if (objects == null || objects.length == 0) {
			return true;
		}

		LoginIpDto loginIp = (LoginIpDto) objects[0];

		log.info("校验当天登录失败的次数是否达到预警的次数:"
				+ ToStringBuilder.reflectionToString(loginIp));
		// 校验当天登录失败的次数是否达到预警的次数
		DateTime nowTime = new DateTime();
		DateTime startOfDay = nowTime.withTimeAtStartOfDay();
		Long loginFailCount = loginIpRepository
				.countByLoginTimeAfterAndLoginTypeAndUser(startOfDay.toDate(),
						LoginType.登录失败, user);
		if (loginFailCount == null || (long) loginFailCount < failDisableCount) {
			return true;
		}
		user.setDisable(Boolean.TRUE);
		user.setDisableReason(UserDisableReason.登录超过限制);
		this.userRepository.save(user);
		/*
		 * this.userRepository.updateUserDisableStatus(user.getId(),
		 * Boolean.TRUE, UserDisableReason.登录超过限制);
		 */
		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {
		return true;
	}

}
