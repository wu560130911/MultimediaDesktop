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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.app.BuiltInApplicationDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.BuiltInApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.BuiltInApplication;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.ApplicationRepository;
import com.wms.studio.repository.BuiltInApplicationRepository;
import com.wms.studio.repository.UserRepository;

/**
 * 
 * @author WMS
 * 
 */
@Service("builtInApplicationService")
public class BuiltInApplicationServiceImpl implements BuiltInApplicationService {

	@Resource
	private BuiltInApplicationRepository builtInApplicationRepository;

	@Resource
	private UserRepository userRepository;

	@Resource
	private ApplicationRepository applicationRepository;

	@Resource
	@Qualifier("builtInApplicationConvert")
	private EntityConvertInterface<BuiltInApplication, BuiltInApplicationDto> builtInApplicationConvert;

	public void setBuiltInApplicationRepository(
			BuiltInApplicationRepository builtInApplicationRepository) {
		this.builtInApplicationRepository = builtInApplicationRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setBuiltInApplicationConvert(
			EntityConvertInterface<BuiltInApplication, BuiltInApplicationDto> builtInApplicationConvert) {
		this.builtInApplicationConvert = builtInApplicationConvert;
	}

	public void setApplicationRepository(
			ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	@Override
	@Transactional
	public CommonResponseDto addBuiltInApplication(String userId,
			List<BuiltInApplicationDto> bapps) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			// 参数校验
			StringUtils.isValidString(userId, "参数错误");
			StringUtils.isValidObject(bapps, "参数错误");
			User user = userRepository.findOne(userId);

			if (user == null) {
				throw new VerificationException("添加用户不存在");
			}

			if (!user.getRole().equals(UserRole.管理员)) {
				throw new VerificationException("没有权限");
			}

			List<BuiltInApplication> toSaveApp = new ArrayList<>(bapps.size());

			for (BuiltInApplicationDto app : bapps) {
				Application dbApp = applicationRepository.findOne(app
						.getApplicationId());
				if (dbApp == null) {
					continue;
				}
				List<BuiltInApplication> t = builtInApplicationRepository
						.findByApplication(dbApp);
				if (t == null || t.isEmpty()) {
					BuiltInApplication bapp = new BuiltInApplication();
					bapp.setApplication(dbApp);
					bapp.setUninstall(app.getUninstall());
					toSaveApp.add(bapp);
				}
			}
			if (!toSaveApp.isEmpty()) {
				builtInApplicationRepository.save(toSaveApp);
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteBuiltInApplication(String userId,
			List<Long> ids) {
		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			// 参数校验
			StringUtils.isValidString(userId, "参数错误");
			StringUtils.isValidObject(ids, "参数错误");
			User user = userRepository.findOne(userId);

			if (user == null) {
				throw new VerificationException("添加用户不存在");
			}

			if (!user.getRole().equals(UserRole.管理员)) {
				throw new VerificationException("没有权限");
			}

			List<BuiltInApplication> toDeletesApp = new ArrayList<>(ids.size());

			for (Long id : ids) {
				BuiltInApplication app = builtInApplicationRepository
						.findOne(id);
				if (app != null) {
					toDeletesApp.add(app);
				}
			}
			if (!toDeletesApp.isEmpty()) {
				builtInApplicationRepository.delete(toDeletesApp);
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public PageDto<BuiltInApplicationDto> findBuiltInApplication(String userId,
			PageSize pageSize) {

		try {

			// 参数校验
			StringUtils.isValidString(userId, "参数错误");
			User user = userRepository.findOne(userId);

			if (user == null) {
				throw new VerificationException("添加用户不存在");
			}

			if (!user.getRole().equals(UserRole.管理员)) {
				throw new VerificationException("没有权限");
			}

			if (pageSize == null) {
				pageSize = new PageSize();
			}

			return builtInApplicationConvert
					.covertToDto(builtInApplicationRepository
							.findAll(new PageRequest(pageSize.getPage() - 1,
									pageSize.getLimit())));

		} catch (VerificationException e) {
		}

		return PageDto.EMPTY_PAGE();
	}
}
