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

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wms.studio.entity.User;

/**
 * @author WMS
 * 
 */
public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User> {

	@Query("select u.name,u.password,u.disable,u.salt,u.vStatus,u.role,u.disableReason from User u where u.id=:id")
	List<Object[]> findUserByIdForLogin(@Param("id") String id);

	User findByEmail(String email);

	@Query(value = "select EXISTS(select 0 from tb_user where email=:email)", nativeQuery = true)
	BigInteger existsByEmail(@Param("email") String email);

	@Query(value = "select u.wallpaper_id from tb_user u where u.id=:id", nativeQuery = true)
	Integer getUserTheme(@Param("id") String userId);

	@Modifying
	@Query("update User u set u.lastLoginTime=:lastLoginTime where u.id=:id")
	int updateUserLastLoginDate(@Param("id") String userId,
			@Param("lastLoginTime") Date loginDate);

	@Modifying
	@Query(value = "update tb_user u set u.wallpaper_id=:wallpaperId where u.id=:id", nativeQuery = true)
	int updateUserWallpaper(@Param("id") String userId,
			@Param("wallpaperId") Integer wallpaperId);

	/*
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update tb_user u set u.disable=:disable and u.disable_reason=:disableReason where u.id=:id"
	 * , nativeQuery = true) int updateUserDisableStatus(@Param("id") String
	 * userId,
	 * 
	 * @Param("disable") Boolean disable,
	 * 
	 * @Param("disableReason") UserDisableReason disableReason);
	 */

	@Modifying
	@Query(value = "update tb_user u set u.credit=u.credit+:credit where u.id=:id", nativeQuery = true)
	int updateUserCreadit(@Param("credit") Integer credit,
			@Param("id") String userId);
}
