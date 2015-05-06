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
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.service.ApplicationService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Application;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.ApplicationRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.utils.SortUtils;

/**
 * 
 * @author WMS
 * 
 */
@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

	private static final Logger log = Logger
			.getLogger(ApplicationServiceImpl.class);

	@Resource
	private ApplicationRepository applicationRepository;

	@Resource
	private UserRepository userRepository;

	@Resource
	@Qualifier("applicationCovert")
	private EntityConvertInterface<Application, ApplicationDto> applicationCovert;

	public void setApplicationCovert(
			EntityConvertInterface<Application, ApplicationDto> applicationCovert) {
		this.applicationCovert = applicationCovert;
	}

	public void setApplicationRepository(
			ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public CommonResponseDto addApplication(ApplicationDto applicationDto) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			Application application = applicationCovert
					.covertToEntity(applicationDto);

			if (application == null) {
				log.info("添加应用接口参数错误，请传入有效的ApplicationDto");
				throw new VerificationException("参数错误");
			}

			// 参数校验
			StringUtils.isValidString(application.getName(), "应用名称不允许为空");
			StringUtils.isValidString(application.getIconCls(), "应用样式不允许为空");
			StringUtils.isValidString(application.getModule(), "应用模块不允许为空");
			StringUtils.isValidString(application.getVersion(), "应用版本号不允许为空");
			StringUtils
					.isValidString(application.getDescription(), "应用描述不允许为空");
			StringUtils.isValidString(application.getTip(), "应用提示不允许为空");
			StringUtils.isValidObject(application.getAddUser(), "添加用户不允许为空");
			StringUtils.isValidObject(application.getTypeGroup(), "应用类型不允许为空");
			StringUtils.isValidObject(application.getRole(), "应用权限不允许为空");

			if (!this.userRepository.exists(applicationDto.getUserId())) {
				throw new VerificationException("添加用户不存在");
			}

			this.applicationRepository.save(application);
			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto modifyApplication(ApplicationDto applicationDto) {
		return addApplication(applicationDto);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<ApplicationDto> findBy(final String userId,
			final String name, final Date startDate, final Date endDate,
			final ApplicationType typeGroup, final List<UserRole> roles,
			final Boolean auditingStatu, PageSize pageSize, SortDto sortDto) {
		if (pageSize == null) {
			pageSize = new PageSize();
		}

		Page<Application> applications = applicationRepository.findAll(
				new Specification<Application>() {

					@Override
					public Predicate toPredicate(Root<Application> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {

						List<Predicate> pres = new ArrayList<Predicate>();

						if (!StringUtils.isBlank(userId)) {
							pres.add(cb.equal(root.get("addUser")
									.as(User.class), new User(userId)));
						}

						if (!StringUtils.isBlank(name)) {
							pres.add(cb.like(root.get("name").as(String.class),
									name));
						}

						// 没办法。Mysql不支持bit查询
						pres.add(cb.equal(
								root.get("auditingStatu").as(Integer.class),
								auditingStatu ? 1 : 0));

						if (startDate != null) {
							pres.add(cb.greaterThanOrEqualTo(root
									.get("addDate").as(Date.class), startDate));
						}

						if (endDate != null) {
							pres.add(cb.lessThanOrEqualTo(root.get("addDate")
									.as(Date.class), endDate));
						}

						if (typeGroup != null) {
							pres.add(cb.equal(
									root.get("typeGroup").as(
											ApplicationType.class), typeGroup));
						}

						if (roles != null && !roles.isEmpty()) {
							In<UserRole> in = cb.in(root.get("role").as(
									UserRole.class));
							for (UserRole role : roles) {
								in.value(role);
							}
							pres.add(in);
						}

						Predicate[] p = new Predicate[pres.size()];
						return cb.and(pres.toArray(p));
					}
				}, new PageRequest(pageSize.getPage() - 1, pageSize.getLimit(),
						SortUtils.covertSortDto(sortDto)));

		return applicationCovert.covertToDto(applications);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<ApplicationDto> findByApplicationType(
			ApplicationType typeGroup, UserRole role, PageSize pageSize,
			SortDto sortDto) {
		if (pageSize == null) {
			pageSize = new PageSize();
		}
		return applicationCovert
				.covertToDto(applicationRepository.findByTypeGroupAndRole(
						typeGroup, role, new PageRequest(
								pageSize.getPage() - 1, pageSize.getLimit(),
								SortUtils.covertSortDto(sortDto))));
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<ApplicationDto> findByUserRole(UserRole role,
			PageSize pageSize, SortDto sortDto) {
		if (pageSize == null) {
			pageSize = new PageSize();
		}
		return applicationCovert.covertToDto(applicationRepository.findByRole(
				role,
				new PageRequest(pageSize.getPage() - 1, pageSize.getLimit(),
						SortUtils.covertSortDto(sortDto))));
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<ApplicationDto> findByAuditingStatu(Boolean auditingStatu,
			PageSize pageSize, SortDto sortDto) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return applicationCovert
				.covertToDto(applicationRepository.findByAuditingStatu(
						auditingStatu,
						new PageRequest(pageSize.getPage() - 1, pageSize
								.getLimit(), SortUtils.covertSortDto(sortDto))));
	}

	@Override
	@Transactional
	@HandlerPoint(handlerName="checkApplicationHandler")
	public CommonResponseDto checkApplication(String userId, List<Long> ids) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			// 参数校验
			StringUtils.isValidString(userId, "用户不为空");
			StringUtils.isValidObject(ids, "应用不为空");
			User user = userRepository.findOne(userId);
			StringUtils.isValidObject(user, "用户不为空");

			if (!UserRole.管理员.equals(user.getRole())) {
				throw new VerificationException("用户没有审核权限");
			}

			//这儿会有批量提交，但是只有一个事务提交，所以如果存在异常，所有都不会有改动
			for (Long id : ids) {
				Application app = applicationRepository.findOne(id);
				if (app != null) {
					app.setAuditingStatu(Boolean.TRUE);
				}
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

}
