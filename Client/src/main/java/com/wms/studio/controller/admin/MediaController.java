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

package com.wms.studio.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.MovieService;
import com.wms.studio.api.service.MusicService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class MediaController {

	private static final Logger log = Logger.getLogger(MediaController.class);

	@Resource
	private MovieService movieService;

	@Resource
	private MusicService musicService;

	public void setMovieService(MovieService movieService) {
		this.movieService = movieService;
	}

	public void setMusicService(MusicService musicService) {
		this.musicService = musicService;
	}

	@RequestMapping(value = "/admin/media/listUnCheckMovies")
	public void listCheckMovie(Model model, PageSize pageSize, String userId,
			String type, String actor, String title,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		PageDto<MovieDto> movies = movieService
				.findBy(title, null, actor, type, userId, Boolean.FALSE,
						startDate, endDate, pageSize, null);

		model.addAttribute("movies", movies.getValues());
		model.addAttribute("total", movies.getTotalElements());
	}

	@RequestMapping(value = "/admin/media/listUnCheckMusics")
	public void listCheckMusic(Model model, PageSize pageSize, String userId,
			String type, String singer, String title,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		PageDto<MusicDto> musics = musicService.findBy(title, singer, type,
				userId, Boolean.FALSE, startDate, endDate, pageSize, null);

		model.addAttribute("musics", musics.getValues());
		model.addAttribute("total", musics.getTotalElements());
	}

	@RequestMapping(value = "/admin/media/checkMovies")
	public void checkMovies(Model model, @RequestBody JSONArray jsonArray) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<Long> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getLong("id"));
			}

			log.debug("管理员[" + userId + "]正在审核视频.");
			CommonResponseDto response = movieService.checkMovies(userId, ids);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}

		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/media/checkMusics")
	public void checkMusics(Model model, @RequestBody JSONArray jsonArray) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<Long> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getLong("id"));
			}

			log.debug("管理员[" + userId + "]正在审核音乐.");
			CommonResponseDto response = musicService.checkMusics(userId, ids);

			if (response == null
					|| response.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}

		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}
}
