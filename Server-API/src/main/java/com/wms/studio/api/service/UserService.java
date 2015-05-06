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

import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.dto.user.UpdateUserDto;
import com.wms.studio.api.dto.user.UserDto;
import com.wms.studio.api.dto.user.UserInfoDto;
import com.wms.studio.api.dto.user.UserLoginDto;

/**
 * @author WMS
 * 
 */
public interface UserService {

	CommonResponseDto registerUser(UserDto userDto);

	CommonResponseDto isValidEmail(String email);

	UserLoginDto login(String id);

	CommonResponseDto updateUserInfo(UpdateUserDto updateUserDto);

	CommonResponseDto checkEmailForUser(String userId, String value);

	CommonResponseDto changeEmail(UpdateUserDto updateUserDto);

	CommonResponseDto changeUserPassword(UpdateUserDto updateUserDto);

	CommonResponseDto findPassword(String email);
	
	CommonResponseDto findPassword(String userId,String password);

	WallpaperDto getUserTheme(String userId);

	void setUserTheme(String userId, Integer wallpaperId);

	UserInfoDto getUserInfo(String userId);

	PageDto<UserInfoDto> listUserInfos(PageSize pageSize, SortDto sortDto);
}
