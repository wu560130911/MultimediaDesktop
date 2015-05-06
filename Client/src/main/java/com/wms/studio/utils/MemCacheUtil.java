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

package com.wms.studio.utils;

import org.apache.shiro.cache.Cache;

import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.constant.SessionKeyConstant;

/**
 * @author WMS
 * 
 */
public class MemCacheUtil {

	public static Object getCacheValueObject(String key) {
		Cache<String, Object> cache = SpringContextHelper
				.getMemcache(SessionKeyConstant.SYSTEM_MEMCACHE_KEY);
		if (cache == null) {
			return null;
		}
		return cache.get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCacheValue(String key) {
		Object value = getCacheValueObject(key);
		if (value == null) {
			return null;
		}
		return (T) value;
	}

	public static <T> void setCache(String key, T value) {
		Cache<String, Object> cache = SpringContextHelper
				.getMemcache(SessionKeyConstant.SYSTEM_MEMCACHE_KEY);
		if (cache == null) {
			return;
		}
		cache.put(key, value);
	}

	public static WallpaperDto getThemeCache(String name) {
		return MemCacheUtil.getCacheValue(SessionKeyConstant.THEME_KEY + "@"
				+ name);
	}

	public static void setThemeCache(String name, WallpaperDto dto) {
		MemCacheUtil.setCache(SessionKeyConstant.THEME_KEY + "@" + name, dto);
	}
}
