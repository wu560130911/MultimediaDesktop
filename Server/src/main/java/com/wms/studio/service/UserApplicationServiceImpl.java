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

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.ListDto;
import com.wms.studio.api.dto.app.UserAppDto;
import com.wms.studio.api.dto.app.UserAppIconDto;
import com.wms.studio.api.dto.app.UserApplicationDto;
import com.wms.studio.api.service.UserApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.User;
import com.wms.studio.entity.UserApplication;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.ApplicationRepository;
import com.wms.studio.repository.UserApplicationRepository;
import com.wms.studio.repository.UserRepository;

/**
 * 
 * @author WMS
 * 
 */
@Service("userApplicationService")
public class UserApplicationServiceImpl implements UserApplicationService {

	@Resource
	private UserRepository userRepository;

	@Resource
	private UserApplicationRepository userApplicationRepository;

	@Resource
	private ApplicationRepository applicationRepository;

	@Resource
	@Qualifier("userAppConvert")
	private EntityConvertInterface<UserApplication, UserAppDto> userAppConvert;

	@Resource
	@Qualifier("userApplicationConvert")
	private EntityConvertInterface<UserApplication, UserApplicationDto> userApplicationConvert;

	public void setApplicationRepository(
			ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	public void setUserAppConvert(
			EntityConvertInterface<UserApplication, UserAppDto> userAppConvert) {
		this.userAppConvert = userAppConvert;
	}

	public void setUserApplicationConvert(
			EntityConvertInterface<UserApplication, UserApplicationDto> userApplicationConvert) {
		this.userApplicationConvert = userApplicationConvert;
	}

	public void setUserApplicationRepository(
			UserApplicationRepository userApplicationRepository) {
		this.userApplicationRepository = userApplicationRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public ListDto<UserAppDto> findDesktopUserApp(String userId) {

		ListDto<UserAppDto> response = new ListDto<>(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			List<UserApplication> apps = userApplicationRepository
					.findByDesktopIconTrueAndUser(new User(userId));
			response.setData(userAppConvert.covertToDto(apps));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public ListDto<UserAppDto> findQuickStartUserApp(String userId) {

		ListDto<UserAppDto> response = new ListDto<>(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			List<UserApplication> apps = userApplicationRepository
					.findByQuickStartTrueAndUser(new User(userId));
			response.setData(userAppConvert.covertToDto(apps));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public ListDto<UserAppDto> findStartMenuUserApp(String userId) {

		ListDto<UserAppDto> response = new ListDto<>(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			List<UserApplication> apps = userApplicationRepository
					.findByStartMenuTrueAndUser(new User(userId));
			response.setData(userAppConvert.covertToDto(apps));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	/**
	 * 先校验该应用是否存在，然后校验应用是否已经安装，最后再执行应用安装
	 */
	@Override
	@Transactional
	public CommonResponseDto addApplication(String userId, Long appId,
			Boolean quickStart, Boolean desktopIcon, Boolean startMenu) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidObject(appId, "应用不允许为空");

			Application app = applicationRepository.findOne(appId);

			StringUtils.isValidObject(app, "应用不存在");

			User user = userRepository.findOne(userId);

			StringUtils.isValidObject(user, "用户不存在");

			List<UserApplication> userApps = userApplicationRepository
					.findByUserAndApp(user, app);

			if (userApps == null || userApps.isEmpty()) {// 说明没有安装此应用
				UserApplication userApp = new UserApplication();
				userApp.setUser(user);
				userApp.setApp(app);
				userApp.setDesktopIcon(desktopIcon);
				userApp.setStartMenu(startMenu);
				userApp.setQuickStart(quickStart);
				userApplicationRepository.save(userApp);
				applicationRepository.addApplicationUseCount(appId);
				response.setResult(UserConstant.SUCCESS);
			} else {
				throw new VerificationException(app.getName() + "已经安装");
			}

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteApplication(String userId, Integer userAppId) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidObject(userAppId, "应用不允许为空");

			UserApplication userApp = userApplicationRepository
					.findOne(userAppId);

			StringUtils.isValidObject(userApp, "未安装该应用");

			if (!userApp.getUninstall()) {
				throw new VerificationException("内置应用不允许卸载");
			}

			if (!userApp.getUser().getId().equals(userId)) {
				throw new VerificationException("不允许修改他人安装的应用");
			}

			userApplicationRepository.delete(userApp);
			applicationRepository.removeApplicationUseCount(userApp.getApp()
					.getId());

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public ListDto<UserApplicationDto> findUserApp(String userId) {

		ListDto<UserApplicationDto> response = new ListDto<>(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			List<UserApplication> apps = userApplicationRepository
					.findByUser(new User(userId));
			response.setData(userApplicationConvert.covertToDto(apps));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto changeUserAppIcon(String userId,
			Integer userAppId, Integer type, Boolean isResult) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidObject(userAppId, "应用不允许为空");
			StringUtils.isValidObject(type, "类型不允许为空");

			UserApplication userApp = userApplicationRepository
					.findOne(userAppId);

			StringUtils.isValidObject(userApp, "未安装该应用");

			if (!userApp.getUser().getId().equals(userId)) {
				throw new VerificationException("不允许修改他人安装的应用");
			}

			boolean isNegative = type < 0;
			type = Math.abs(type);
			if (isResult) {// 直接进行判断，作为最终结果，可能的结果有1,2,3,4,5,6,7

				userApp.setQuickStart(((type & 1) == 1));
				userApp.setStartMenu((type & 2) == 2);
				userApp.setDesktopIcon((type & 4) == 4);

			} else {// 只是单独修改，可能的值有1,2,4,-1,-2,-4
				if ((type & 1) == 1) {
					userApp.setQuickStart(!isNegative);
				} else if ((type & 2) == 2) {
					userApp.setStartMenu(!isNegative);
				} else if ((type & 4) == 4) {
					userApp.setDesktopIcon(!isNegative);
				}
			}
			// type==1代表增加到快速启动栏
			// type==2代表增加到开始菜单
			// type==4代表增加到桌面
			// type==3代表增加到快速启动栏和开始菜单
			// type==5代表增加到快速启动栏和桌面
			// type==6代表增加到开始菜单和桌面
			// type==7代表增加到快速启动栏和开始菜单和桌面

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteApplication(String userId,
			List<Integer> userAppIds) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidObject(userAppIds, "应用不允许为空");

			if (userAppIds.isEmpty()) {
				throw new VerificationException("应用不允许为空");
			}

			for (Integer id : userAppIds) {
				deleteApplication(userId, id);
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto changeUserAppIcon(String userId,
			List<UserAppIconDto> userApps, Boolean isResult) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {
			StringUtils.isValidString(userId, "账号不允许为空");
			StringUtils.isValidObject(userApps, "应用不允许为空");

			for (UserAppIconDto dto : userApps) {
				changeUserAppIcon(userId, dto.getUserAppId(), dto.getType(),
						isResult);
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

}
