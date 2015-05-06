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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.ListDto;
import com.wms.studio.api.dto.media.MoviePlaylistDto;
import com.wms.studio.api.dto.media.MusicPlaylistDto;
import com.wms.studio.api.service.MoviePlaylistService;
import com.wms.studio.api.service.MusicPlaylistService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class PlaylistController {

	private static final Logger log = Logger
			.getLogger(PlaylistController.class);

	@Resource
	private MoviePlaylistService moviePlaylistService;

	@Resource
	private MusicPlaylistService musicPlaylistService;

	public void setMoviePlaylistService(
			MoviePlaylistService moviePlaylistService) {
		this.moviePlaylistService = moviePlaylistService;
	}

	public void setMusicPlaylistService(
			MusicPlaylistService musicPlaylistService) {
		this.musicPlaylistService = musicPlaylistService;
	}

	@RequestMapping(value = "/playlist/movies/add")
	public void addMoviePlaylist(Model model, @RequestBody JSONArray jsonArray) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<Long> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getLong("id"));
			}

			log.debug("用户[" + userId + "]正在添加视频播放列表.");
			CommonResponseDto response = moviePlaylistService.addPlaylist(
					userId, ids);
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

	@RequestMapping(value = "/playlist/music/add")
	public void addMusicPlaylist(Model model, @RequestBody JSONArray jsonArray) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			StringUtils.isValidObject(jsonArray, "参数错误");

			List<Long> ids = new ArrayList<>(jsonArray.size());

			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add(jsonArray.getJSONObject(i).getLong("id"));
			}

			log.debug("用户[" + userId + "]正在添加音乐播放列表.");
			CommonResponseDto response = musicPlaylistService.addPlaylist(
					userId, ids);
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

	@RequestMapping(value = "/playlist/movies/list")
	public void getMoviePlaylists(Model model) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			ListDto<MoviePlaylistDto> movies = moviePlaylistService
					.findByUser(userId);

			if (movies == null || movies.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(movies.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("datas", movies.getData());
			}

		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/playlist/musics/list")
	public void getMusicPlaylists(Model model) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			ListDto<MusicPlaylistDto> musics = musicPlaylistService
					.findByUser(userId);

			if (musics == null || musics.getResult() != UserConstant.SUCCESS) {
				throw new VerificationException(musics.getErrorMessage());
			} else {
				model.addAttribute("success", true);
				model.addAttribute("datas", musics.getData());
			}

		} catch (VerificationException e) {
			model.addAttribute("success", false);
			model.addAttribute("error", e.getMessage());
		}
	}

	@RequestMapping(value = "/playlist/movies/delete")
	public void deleteMoviePlaylists(Model model, Long mediaId) {
		try {
			String userId = UserUtils.getCurrentUserId();
			StringUtils.isValidString(userId, "账号没有登录");

			CommonResponseDto response = moviePlaylistService.deletePlaylist(
					userId, mediaId);

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
