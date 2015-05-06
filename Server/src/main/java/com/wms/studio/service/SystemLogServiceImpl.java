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
import java.util.Collections;
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
import com.wms.studio.api.dto.SystemLogDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.OrderDto;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.dto.sort.SortDto.Direction;
import com.wms.studio.api.service.SystemLogService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.SystemLog;
import com.wms.studio.repository.SystemLogRepository;
import com.wms.studio.utils.SortUtils;

/**
 * 
 * @author WMS
 * 
 */
@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService {

	@Resource
	private SystemLogRepository systemLogRepository;

	@Resource
	@Qualifier("systemLogConvert")
	private EntityConvertInterface<SystemLog, SystemLogDto> systemLogConvert;

	public void setSystemLogRepository(SystemLogRepository systemLogRepository) {
		this.systemLogRepository = systemLogRepository;
	}

	public void setSystemLogConvert(
			EntityConvertInterface<SystemLog, SystemLogDto> systemLogConvert) {
		this.systemLogConvert = systemLogConvert;
	}

	@Override
	public List<String> getLogServer() {

		List<SystemLog> logs = systemLogRepository
				.findAll(new Specification<SystemLog>() {

					@Override
					public Predicate toPredicate(Root<SystemLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						/*
						 * CriteriaQuery<Tuple> cq =cb.createQuery(Tuple.class);
						 * cq.from(SystemLog.class);
						 * cq.groupBy(root.get("logFrom"));
						 * cq.select(cb.tuple(root.get("logFrom")));
						 */
						query.groupBy(root.get("logFrom"));
						return null;
					}
				});

		if (logs == null || logs.size() <= 0) {
			return Collections.emptyList();
		}

		ArrayList<String> logServers = new ArrayList<String>(logs.size());
		for (SystemLog log : logs) {
			logServers.add(log.getLogFrom());
		}
		return logServers;
	}

	@Override
	public PageDto<SystemLogDto> findBy(final String logFrom,
			final String classPath, final String message,
			final String threadName, final String lever, final Date start,
			final Date end, PageSize pageSize, SortDto sortDto) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		OrderDto order = new OrderDto(Direction.ASC, "logDateTime");

		if (sortDto == null) {
			sortDto = new SortDto(order);
		}

		return systemLogConvert.covertToDto(systemLogRepository.findAll(
				new Specification<SystemLog>() {

					@Override
					public Predicate toPredicate(Root<SystemLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {

						List<Predicate> pres = new ArrayList<Predicate>();

						if (StringUtils.isNotBlank(logFrom)) {
							pres.add(cb.equal(
									root.get("logFrom").as(String.class),
									logFrom));
						}

						if (StringUtils.isNotBlank(classPath)) {
							pres.add(cb.like(
									root.get("classPath").as(String.class), "%"
											+ classPath + "%"));
						}

						if (StringUtils.isNotBlank(message)) {
							pres.add(cb.like(
									root.get("message").as(String.class), "%"
											+ message + "%"));
						}

						if (StringUtils.isNotBlank(threadName)) {
							pres.add(cb.like(
									root.get("threadName").as(String.class),
									"%" + threadName + "%"));
						}

						if (StringUtils.isNotBlank(lever)) {
							pres.add(cb.equal(root.get("lever")
									.as(String.class), lever));
						}

						if (start != null) {
							pres.add(cb.greaterThanOrEqualTo(
									root.get("logDateTime").as(Date.class),
									start));
						}

						if (end != null) {
							pres.add(cb
									.lessThanOrEqualTo(root.get("logDateTime")
											.as(Date.class), end));
						}

						Predicate[] p = new Predicate[pres.size()];
						return cb.and(pres.toArray(p));
					}
				}, new PageRequest(pageSize.getPage() - 1, pageSize.getLimit(),
						SortUtils.covertSortDto(sortDto))));
	}

}
