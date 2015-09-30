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
package com.wms.studio.service.api;


import com.wms.studio.domain.transfer.CommonResponseDto;
import com.wms.studio.domain.transfer.ListDto;
import com.wms.studio.domain.transfer.app.UserAppDto;
import com.wms.studio.domain.transfer.app.UserAppIconDto;
import com.wms.studio.domain.transfer.app.UserApplicationDto;

import java.util.List;

/**
 * @author WMS
 * 
 */
public interface UserApplicationService {

	/**
	 * 查找指定用户的桌面快捷方式
	 * 
	 * @param userId
	 * @return
	 */
	ListDto<UserAppDto> findDesktopUserApp(String userId);

	/**
	 * 查找指定用户的快速启动栏的应用
	 * 
	 * @param userId
	 * @return
	 */

	ListDto<UserAppDto> findQuickStartUserApp(String userId);

	/**
	 * 查找指定用户的开始菜单的应用
	 * 
	 * @param userId
	 * @return
	 */
	ListDto<UserAppDto> findStartMenuUserApp(String userId);

	/**
	 * 查找指定用户的应用
	 * 
	 * @param userId
	 * @return
	 */
	ListDto<UserApplicationDto> findUserApp(String userId);

	/**
	 * 
	 * @param userId
	 * @param userAppId
	 * @param type
	 * @return
	 */
	CommonResponseDto changeUserAppIcon(String userId, Integer userAppId,
										Integer type, Boolean isResult);
	
	CommonResponseDto changeUserAppIcon(String userId, List<UserAppIconDto> userApps, Boolean isResult);

	/**
	 * 添加应用
	 * 
	 * @param userId
	 * @param appId
	 * @param quickStart
	 * @param desktopIcon
	 * @param startMenu
	 * @return
	 */
	CommonResponseDto addApplication(String userId, Long appId,
									 Boolean quickStart, Boolean desktopIcon, Boolean startMenu);

	CommonResponseDto deleteApplication(String userId, Integer userAppId);
	
	CommonResponseDto deleteApplication(String userId, List<Integer> userAppIds);
}
