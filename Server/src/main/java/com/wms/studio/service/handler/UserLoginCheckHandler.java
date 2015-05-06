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

package com.wms.studio.service.handler;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.entity.User;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
@Service("userLoginCheckHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(handlerName = "userLoginHandler", scope = HandlerScope.Before, beanName = "userLoginCheckHandler")
public class UserLoginCheckHandler extends AbstractHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean beforeHandler(HandlerData data) {

		Object[] objects = data.getValue("args");

		if (objects == null || objects.length == 0) {
			return true;
		}

		String userId = (String) objects[0];

		if (!StringUtils.isLengthValid(userId, UserConstant.USER_ID_MIN_LENGTH,
				UserConstant.USER_ID_MAX_LENGTH)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean handler(HandlerData data) {

		Object[] objects = data.getValue("args");

		String userId = (String) objects[0];
		
		if (!userRepository.exists(userId)) {
			return true;
		}
		
		User user = userRepository.findOne(userId);
		if (user.isDisable() && user.isvStatus()
				&& UserDisableReason.登录超过限制.equals(user.getDisableReason())) {
			DateTime startOfDay = new DateTime().withTimeAtStartOfDay();
			if (startOfDay.isAfter(user.getLastLoginTime().getTime())) {
				user.setDisable(false);
				user.setDisableReason(UserDisableReason.正常);
				this.userRepository.save(user);
			}
		}
		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {
		return true;
	}

}
