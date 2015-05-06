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
package com.wms.studio.api.service;

import java.util.Date;
import java.util.List;

import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.app.ApplicationDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;

/**
 * @author WMS
 * 
 */
public interface ApplicationService {

	CommonResponseDto addApplication(ApplicationDto applicationDto);

	CommonResponseDto modifyApplication(ApplicationDto applicationDto);

	CommonResponseDto checkApplication(String userId, List<Long> ids);

	PageDto<ApplicationDto> findBy(String userId, String name, Date startDate,
			Date endDate, ApplicationType typeGroup, List<UserRole> roles,
			Boolean auditingStatu, PageSize pageSize, SortDto sortDto);

	PageDto<ApplicationDto> findByApplicationType(ApplicationType typeGroup,
			UserRole role, PageSize pageSize, SortDto sortDto);

	PageDto<ApplicationDto> findByUserRole(UserRole role, PageSize pageSize,
			SortDto sortDto);

	PageDto<ApplicationDto> findByAuditingStatu(Boolean auditingStatu,
			PageSize pageSize, SortDto sortDto);

}
