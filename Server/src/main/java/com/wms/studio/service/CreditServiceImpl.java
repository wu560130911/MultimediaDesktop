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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.user.CreditDto;
import com.wms.studio.api.service.CreditService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Credit;
import com.wms.studio.entity.User;
import com.wms.studio.repository.CreditRepository;

@Service("creditService")
public class CreditServiceImpl implements CreditService {

	@Resource
	private CreditRepository creditRepository;

	@Resource
	@Qualifier("creditCovert")
	private EntityConvertInterface<Credit, CreditDto> creditConvert;

	@Override
	public PageDto<CreditDto> findBy(final String userId, final Date start,
			final Date end, PageSize pageSize) {
		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return creditConvert
				.covertToDto(creditRepository.findAll(
						new Specification<Credit>() {

							@Override
							public Predicate toPredicate(Root<Credit> root,
									CriteriaQuery<?> query, CriteriaBuilder cb) {
								List<Predicate> pres = new ArrayList<>(3);

								if (start != null) {
									pres.add(cb.greaterThanOrEqualTo(
											root.get("changeTime").as(
													Date.class), start));
								}

								if (end != null) {
									pres.add(cb.lessThanOrEqualTo(
											root.get("changeTime").as(
													Date.class), end));
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
