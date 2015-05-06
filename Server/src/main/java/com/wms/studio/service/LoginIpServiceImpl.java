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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.user.LoginIpDto;
import com.wms.studio.api.dto.user.LoginIpInfoDto;
import com.wms.studio.api.service.LoginIpService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.LoginIp;
import com.wms.studio.entity.User;
import com.wms.studio.repository.LoginIpRepository;
import com.wms.studio.repository.UserRepository;

/**
 * 
 * @author WMS
 * 
 */
@Service("loginIpService")
public class LoginIpServiceImpl implements LoginIpService {

	private static final Logger log = Logger
			.getLogger(LoginIpServiceImpl.class);

	@Resource
	private LoginIpRepository loginIpRepository;

	@Resource
	private UserRepository userRepository;

	@Resource
	@Qualifier("loginIpConvert")
	private EntityConvertInterface<LoginIp, LoginIpInfoDto> loginIpConvert;

	public void setLoginIpRepository(LoginIpRepository loginIpRepository) {
		this.loginIpRepository = loginIpRepository;
	}

	public void setLoginIpConvert(
			EntityConvertInterface<LoginIp, LoginIpInfoDto> loginIpConvert) {
		this.loginIpConvert = loginIpConvert;
	}

	@Transactional
	@Override
	@HandlerPoint(handlerName = "loginHandler")
	public void addLoginIp(LoginIpDto loginIpdto) {

		if (loginIpdto == null || StringUtils.isBlank(loginIpdto.getUserId())
				|| StringUtils.isBlank(loginIpdto.getIpaddress())
				|| loginIpdto.getLoginType() == null) {
			log.error("[登录记录]-[登录记录数据校验失败]-[校验数据为:]"
					+ ToStringBuilder.reflectionToString(loginIpdto));
			return;
		}

		LoginIp loginIp = new LoginIp(new User(loginIpdto.getUserId()),
				loginIpdto.getIpaddress(), loginIpdto.getLoginType());
		loginIpRepository.save(loginIp);
		userRepository.updateUserLastLoginDate(loginIpdto.getUserId(),
				loginIp.getLoginTime());
	}

	@Override
	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<LoginIpInfoDto> findBy(final String userId,
			final Date start, final Date end, PageSize pageSize) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return loginIpConvert
				.covertToDto(loginIpRepository.findAll(
						new Specification<LoginIp>() {

							@Override
							public Predicate toPredicate(Root<LoginIp> root,
									CriteriaQuery<?> query, CriteriaBuilder cb) {
								List<Predicate> pres = new ArrayList<>(3);

								if (start != null) {
									pres.add(cb.greaterThanOrEqualTo(
											root.get("loginTime")
													.as(Date.class), start));
								}

								if (end != null) {
									pres.add(cb.lessThanOrEqualTo(
											root.get("loginTime")
													.as(Date.class), end));
								}

								if (!StringUtils.isBlank(userId)) {
									pres.add(cb.equal(
											root.get("user").as(User.class),
											new User(userId)));
								}

								Predicate[] p = new Predicate[pres.size()];
								return cb.and(pres.toArray(p));
							}
						},
						new PageRequest(pageSize.getPage() - 1, pageSize
								.getLimit())));
	}
}
