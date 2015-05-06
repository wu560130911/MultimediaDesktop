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

package com.wms.studio.service.handler.wallpaper;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.entity.Credit;
import com.wms.studio.entity.User;
import com.wms.studio.repository.CreditRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.repository.WallpaperRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
@Service("wallpaperAddCreditHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(beanName = "wallpaperAddCreditHandler", handlerName = "wallpaperAddHandler", scope = HandlerScope.After)
public class WallpaperAddCreditHandler extends AbstractHandler {

	private static final Logger log = Logger
			.getLogger(WallpaperAddCreditHandler.class);

	@Value("#{props['system.wallpaper.maxAddCredit']}")
	private Integer maxAddCredit;

	@Value("#{credits['credit.add.wallpaper']}")
	private Integer addCredit;

	@Autowired
	private CreditRepository creditRepository;

	@Autowired
	private WallpaperRepository wallpaperRepository;

	@Autowired
	private UserRepository userRepository;

	public void setMaxAddCredit(Integer maxAddCredit) {
		this.maxAddCredit = maxAddCredit;
	}

	public void setAddCredit(Integer addCredit) {
		this.addCredit = addCredit;
	}

	public void setCreditRepository(CreditRepository creditRepository) {
		this.creditRepository = creditRepository;
	}

	public void setWallpaperRepository(WallpaperRepository wallpaperRepository) {
		this.wallpaperRepository = wallpaperRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean beforeHandler(HandlerData data) {

		log.info("[Handler]-[WallpaperAddCreditHandler]-添加壁纸积分处理逻辑");

		Object[] objects = getArgs(data);

		if (objects == null || objects.length == 0) {
			return true;
		}

		if (maxAddCredit == null || addCredit == null || addCredit <= 0
				|| maxAddCredit <= 0) {
			log.info("[Handler]-[WallpaperAddCreditHandler]-管理员将添加壁纸奖励积分关闭");
			return true;
		}

		CommonResponseDto response = getResult(data);
		WallpaperDto wallpaperDto = (WallpaperDto) objects[0];

		if (response == null || wallpaperDto == null) {
			return true;
		}

		if (UserConstant.SUCCESS != response.getResult()) {
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

		WallpaperDto wallpaperDto = (WallpaperDto) objects[0];

		DateTime nowTime = new DateTime();
		DateTime startOfDay = nowTime.withTimeAtStartOfDay();

		Long count = wallpaperRepository.countByAddDateAfterAndUser(
				startOfDay.toDate(), new User(wallpaperDto.getUserId()));

		if (count == null || count.intValue() < maxAddCredit) {
			Credit credit = new Credit(new User(wallpaperDto.getUserId()),
					com.wms.studio.api.constant.CreditType.收入);
			credit.setCreditNum(addCredit);
			credit.setDescriptions("上传壁纸奖励");
			userRepository.updateUserCreadit(credit.getCreditNum(),
					wallpaperDto.getUserId());
			creditRepository.save(credit);
		}

		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {
		return false;
	}

}
