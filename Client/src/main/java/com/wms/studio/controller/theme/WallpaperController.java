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

package com.wms.studio.controller.theme;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.WallpaperEnum;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.UserService;
import com.wms.studio.api.service.WallpaperService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.FileUtils;
import com.wms.studio.utils.MemCacheUtil;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class WallpaperController {

	private static final Logger log = Logger
			.getLogger(WallpaperController.class);

	@Autowired
	private WallpaperService wallpaperService;

	@Autowired
	private UserService userService;

	@Value("#{props['server.wallpaperPath']}")
	private String wallpaperPath;

	@Value("#{props['server.basePath']}")
	private String basePath;

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setWallpaperPath(String wallpaperPath) {
		this.wallpaperPath = wallpaperPath;
	}

	public void setWallpaperService(WallpaperService wallpaperService) {
		this.wallpaperService = wallpaperService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/wallpaper/list")
	public void listWallpaper(Model model, Integer page, Integer limit,
			WallpaperEnum type,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		PageSize pageSize = new PageSize(page, limit);

		PageDto<WallpaperDto> wallpapers = wallpaperService.findBy(type,
				startDate, endDate, pageSize);

		model.addAttribute("wallpapers", wallpapers.getValues());
		model.addAttribute("total", wallpapers.getTotalElements());
	}

	@RequestMapping(value = "/wallpaper/add", method = RequestMethod.POST)
	public void addWallpaper(Model model, MultipartFile imageFile,
			WallpaperEnum type) {
		if (imageFile.isEmpty() || type == null) {
			model.addAttribute("success", "false");
			model.addAttribute("error", "参数校验错误");
			return;
		}
		try {
			File image = FileUtils.getImageFile(basePath + wallpaperPath,
					imageFile.getOriginalFilename());
			if (image == null) {
				model.addAttribute("success", "false");
				model.addAttribute("error", "图片文件错误或者系统异常");
				return;
			}
			imageFile.transferTo(image);
			WallpaperDto dto = new WallpaperDto();
			dto.setPath(wallpaperPath + image.getName());
			dto.setUserId(UserUtils.getCurrentUserId());
			dto.setWallpaperType(type);
			CommonResponseDto response = wallpaperService.addWallpaper(dto);
			if (response.getResult() == UserConstant.SUCCESS) {
				model.addAttribute("success", "true");
			} else {
				model.addAttribute("success", "false");
				model.addAttribute("error", response.getErrorMessage());
			}
		} catch (IllegalStateException | IOException e) {
			log.fatal("添加壁纸异常，请管理员检查", e);
			model.addAttribute("success", "false");
			model.addAttribute("error", "系统异常");
		}
	}

	@RequestMapping(value = "/wallpaper/change", method = RequestMethod.POST)
	public void changeWallpaper(Model model, WallpaperDto dto) {

		try {
			
			StringUtils.isValidObject(dto, "参数异常");
			StringUtils.isValidString(dto.getPath(), "参数异常");

			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "登录用户获取异常");
			
			MemCacheUtil.setThemeCache(userId, dto);

			// 可以在业务逻辑系统中设置定时将用户主题持久化到数据库，而不是每操作一次都进行DB修改
			userService.setUserTheme(userId, dto.getId());
			model.addAttribute("success", true);
			dto = null;
		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}
}
