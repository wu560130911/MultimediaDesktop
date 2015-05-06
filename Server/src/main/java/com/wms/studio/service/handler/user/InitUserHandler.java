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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wms.studio.annotations.Handler;
import com.wms.studio.annotations.HandlerScope;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.user.UserDto;
import com.wms.studio.entity.BuiltInApplication;
import com.wms.studio.entity.User;
import com.wms.studio.entity.UserApplication;
import com.wms.studio.entity.Wallpaper;
import com.wms.studio.repository.ApplicationRepository;
import com.wms.studio.repository.BuiltInApplicationRepository;
import com.wms.studio.repository.UserApplicationRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.repository.WallpaperRepository;
import com.wms.studio.service.handler.api.AbstractHandler;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
@Service("initUserHandler")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Handler(beanName = "initUserHandler", handlerName = "registerUserHandler", scope = HandlerScope.After)
public class InitUserHandler extends AbstractHandler {

	private static final Logger log = Logger.getLogger(InitUserHandler.class);

	@Resource
	private UserRepository userRepository;

	@Resource
	private WallpaperRepository wallpaperRepository;

	@Resource
	private BuiltInApplicationRepository builtInApplicationRepository;

	@Resource
	private UserApplicationRepository userApplicationRepository;

	@Resource
	private ApplicationRepository applicationRepository;

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setWallpaperRepository(WallpaperRepository wallpaperRepository) {
		this.wallpaperRepository = wallpaperRepository;
	}

	public void setBuiltInApplicationRepository(
			BuiltInApplicationRepository builtInApplicationRepository) {
		this.builtInApplicationRepository = builtInApplicationRepository;
	}

	public void setUserApplicationRepository(
			UserApplicationRepository userApplicationRepository) {
		this.userApplicationRepository = userApplicationRepository;
	}

	public void setApplicationRepository(
			ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
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

		List<Wallpaper> wallpapers = wallpaperRepository.findByIsDefaultTrue();

		if (wallpapers != null && wallpapers.size() > 0) {
			user.setWallpaper(wallpapers.get(0));
		} else {
			log.fatal("请管理员设置默认的壁纸");
		}

		List<BuiltInApplication> builtInApps = builtInApplicationRepository
				.findAll();

		if (builtInApps != null && builtInApps.size() > 0) {
			List<UserApplication> apps = new ArrayList<>(builtInApps.size());
			for (BuiltInApplication builtInApp : builtInApps) {
				UserApplication app = new UserApplication();
				app.setUninstall(builtInApp.getUninstall());
				app.setDesktopIcon(Boolean.TRUE);
				app.setApp(builtInApp.getApplication());
				app.setQuickStart(Boolean.FALSE);
				app.setStartMenu(Boolean.TRUE);
				app.setUser(user);
				apps.add(app);
				applicationRepository.addApplicationUseCount(builtInApp
						.getApplication().getId());
			}
			userApplicationRepository.save(apps);
		}

		return true;
	}

	@Override
	public boolean afterHandler(HandlerData data) {
		return false;
	}

}
