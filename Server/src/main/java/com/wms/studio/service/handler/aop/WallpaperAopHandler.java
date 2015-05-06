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

package com.wms.studio.service.handler.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Value;

import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
/*@Component
@Aspect
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)*/
@Deprecated
public class WallpaperAopHandler extends AbstractAopHandler<HandlerData> {

	private static final String TAG = "execution(* com.wms.studio.api.service.WallpaperService.addWallpaper(com.wms.studio.api.dto.WallpaperDto)) && args(wallpaperDto)";

	@Value("#{props['system.wallpaper.handler']}")
	private String handlersName;

	public void setHandlersName(String handlersName) {
		this.handlersName = handlersName;
	}

	@Override
	public String getHandlersName() {
		return handlersName;
	}

	@AfterReturning(value = TAG, returning = "response")
	public void afterAddWallpaper(CommonResponseDto response,
			WallpaperDto wallpaperDto) {
		HandlerData data = new HandlerData();
		data.put("response", response);
		data.put("wallpaperDto", wallpaperDto);
		excuteHandler(data);
	}
}
