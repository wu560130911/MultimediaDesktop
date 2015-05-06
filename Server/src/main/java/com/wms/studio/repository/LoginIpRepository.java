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

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wms.studio.api.constant.LoginType;
import com.wms.studio.entity.LoginIp;
import com.wms.studio.entity.User;

/**
 * @author WMS
 * 
 */
public interface LoginIpRepository extends JpaRepository<LoginIp, Long>,JpaSpecificationExecutor<LoginIp> {

	List<LoginIp> findByLoginTimeBetweenAndUser(Date start, Date end,User user);

	Page<LoginIp> findByLoginTimeBetweenAndUser(Date start, Date end, Pageable pageable,User user);

	List<LoginIp> findByLoginTimeAfterAndUser(Date start,User user);

	Page<LoginIp> findByLoginTimeAfterAndUser(Date start, Pageable pageable,User user);

	List<LoginIp> findByLoginTimeAfterAndLoginTypeAndUser(Date start,
			LoginType loginType,User user);

	Page<LoginIp> findByLoginTimeAfterAndLoginTypeAndUser(Date start,
			LoginType loginType, Pageable pageable,User user);

	List<LoginIp> findByLoginTimeBeforeAndUser(Date start,User user);

	Page<LoginIp> findByLoginTimeBeforeAndUser(Date start, Pageable pageable,User user);

	Long countByLoginTimeAfterAndLoginTypeAndUser(Date start, LoginType loginType,User user);

	Long countByLoginTimeBeforeAndUser(Date start,User user);
}
