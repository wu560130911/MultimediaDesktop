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
package com.wms.studio.covert;

import org.springframework.stereotype.Component;

import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.entity.User;
import com.wms.studio.entity.Wallpaper;

/**
 * 
 * @author WMS
 * 
 */
@Component("wallpaperCovert")
public class WallpaperCovert extends AbstractCovert<Wallpaper, WallpaperDto> {

	@Override
	public WallpaperDto covertToDto(Wallpaper e) {

		if (e == null) {
			return null;
		}

		WallpaperDto dto = new WallpaperDto();

		dto.setAddDate(e.getAddDate());
		dto.setDefault(e.isDefault());
		dto.setId(e.getId());
		dto.setPath(e.getPath());
		dto.setWallpaperType(e.getWallpaperType());
		dto.setUserId(e.getUser().getId());

		return dto;
	}

	@Override
	public Wallpaper covertToEntity(WallpaperDto d) {
		if (d == null) {
			return null;
		}

		Wallpaper e = new Wallpaper();

		e.setAddDate(d.getAddDate());
		e.setDefault(d.isDefault());
		e.setId(d.getId());
		e.setPath(d.getPath());
		e.setWallpaperType(d.getWallpaperType());
		e.setUser(new User(d.getUserId()));

		return e;
	}

}
