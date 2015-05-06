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

package com.wms.studio.service.handler.aop;

import org.aspectj.lang.annotation.Before;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.entity.User;
import com.wms.studio.repository.UserRepository;

/**
 * @author WMS
 * 
 *         处理一天登录超过5次后，不允许登录的逻辑，但是登录前需要检查是否为今日超过限制
 * 
 */
/*@Component
@Aspect*/
@Deprecated
public class UserLoginAopHandler {

	private static final String TAG = "execution(* com.wms.studio.api.service.UserService.login(String)) && args(id)";

	@Autowired
	private UserRepository userRepository;

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Before(value = TAG)
	public void beforeLogin(String id) {

		if (!StringUtils.isLengthValid(id, UserConstant.USER_ID_MIN_LENGTH,
				UserConstant.USER_ID_MAX_LENGTH)) {
			return;
		}

		User user = userRepository.findOne(id);

		if (user == null) {
			return;
		}

		if (user.isDisable() && user.isvStatus()
				&& UserDisableReason.登录超过限制.equals(user.getDisableReason())) {
			DateTime startOfDay = new DateTime().withTimeAtStartOfDay();
			if (startOfDay.isAfter(user.getLastLoginTime().getTime())) {
				user.setDisable(false);
				user.setDisableReason(UserDisableReason.正常);
				this.userRepository.save(user);
			}
		}
	}
}
