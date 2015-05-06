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

package com.wms.studio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wms.studio.api.constant.ApplicationType;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.entity.Application;

/**
 * @author WMS
 * 
 */
public interface ApplicationRepository extends
		JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

	Page<Application> findByTypeGroupAndRole(ApplicationType typeGroup,
			UserRole role, Pageable pageable);

	Page<Application> findByRole(UserRole role, Pageable pageable);

	Page<Application> findByAuditingStatu(boolean auditingStatu,
			Pageable pageable);

	@Modifying
	@Query(value = "update tb_application app set app.use_count=app.use_count+1 where app.id=:id", nativeQuery = true)
	int addApplicationUseCount(@Param("id") Long appId);

	@Modifying
	@Query(value = "update tb_application app set app.use_count=app.use_count-1 where app.id=:id", nativeQuery = true)
	int removeApplicationUseCount(@Param("id") Long appId);
}
