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

package com.wms.studio.controller.media;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.MusicService;
import com.wms.studio.async.MusicAsyncCallable;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class MusicController {

	private static final Logger log = Logger.getLogger(MusicController.class);

	@Resource
	private MusicService musicService;

	@Value("#{props['server.mediaBasePath']}")
	private String filePath;

	@Value("#{props['server.fileuploadTime']}")
	private Long fileUploadTime;

	public void setFileUploadTime(Long fileUploadTime) {
		this.fileUploadTime = fileUploadTime;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setMusicService(MusicService musicService) {
		this.musicService = musicService;
	}

	@RequestMapping(value = "/media/music/add", method = RequestMethod.POST)
	public WebAsyncTask<Void> addMovie(Model model, MultipartFile musicFile,
			MusicDto musicDto) {

		log.info("用户[" + UserUtils.getCurrentUserId() + "]正在上传音乐"
				+ ToStringBuilder.reflectionToString(musicDto));

		return new WebAsyncTask<>(fileUploadTime, new MusicAsyncCallable(model,
				musicFile, musicDto, filePath, musicService));
	}

	@RequestMapping(value = "/media/music/list")
	public void listMovie(Model model, PageSize pageSize, String userId,
			String type, String singer, String title,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		PageDto<MusicDto> musics = musicService.findBy(title, singer, type,
				userId, Boolean.TRUE, startDate, endDate, pageSize, null);

		model.addAttribute("musics", musics.getValues());
		model.addAttribute("total", musics.getTotalElements());
	}

}
