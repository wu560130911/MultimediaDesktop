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
package com.wms.studio.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.ListDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MusicPlaylistDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.service.MusicPlaylistService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Music;
import com.wms.studio.entity.MusicPlaylist;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.MusicPlaylistRepository;
import com.wms.studio.repository.MusicRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.utils.SortUtils;

@Service("musicPlaylistService")
public class MusicPlaylistServiceImpl implements MusicPlaylistService {

	@Resource
	private UserRepository userRepository;

	@Resource
	private MusicRepository musicRepository;

	@Resource
	private MusicPlaylistRepository musicPlaylistRepository;

	@Resource
	@Qualifier("musicPlaylistConvert")
	private EntityConvertInterface<MusicPlaylist, MusicPlaylistDto> musicPlaylistConvert;

	public void setMusicPlaylistConvert(
			EntityConvertInterface<MusicPlaylist, MusicPlaylistDto> musicPlaylistConvert) {
		this.musicPlaylistConvert = musicPlaylistConvert;
	}

	public void setMusicPlaylistRepository(
			MusicPlaylistRepository musicPlaylistRepository) {
		this.musicPlaylistRepository = musicPlaylistRepository;
	}

	public void setMusicRepository(MusicRepository musicRepository) {
		this.musicRepository = musicRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public CommonResponseDto addPlaylist(String userId, Long mediaId) {
		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (mediaId == null || mediaId <= 0L) {
				throw new VerificationException("资源编号错误");
			}
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}
			if (!musicRepository.exists(mediaId)) {
				throw new VerificationException("资源编号不存在");
			}

			User user = new User(userId);
			Music music = new Music(mediaId);

			List<MusicPlaylist> lists = musicPlaylistRepository
					.findByUserAndMusic(user, music);
			if (lists == null || lists.isEmpty()) {
				MusicPlaylist list = new MusicPlaylist();
				list.setUser(user);
				list.setMusic(music);
				this.musicPlaylistRepository.save(list);
				response.setResult(UserConstant.SUCCESS);
			} else {
				throw new VerificationException("已经在收藏列表中");
			}
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	public CommonResponseDto deletePlaylist(String userId, Long id) {
		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (id == null || id <= 0L) {
				throw new VerificationException("资源编号错误");
			}
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			MusicPlaylist list = musicPlaylistRepository.findOne(id);

			if (list == null) {
				throw new VerificationException("资源编号错误");
			}

			if (list.getUser().getId().equals(userId)) {
				musicPlaylistRepository.delete(list);
				response.setResult(UserConstant.SUCCESS);
			} else {
				throw new VerificationException("没有权限");
			}

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	public CommonResponseDto deletePlaylistByUser(String userId) {
		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			List<MusicPlaylist> lists = musicPlaylistRepository
					.findByUser(new User(userId));

			if (lists != null && !lists.isEmpty()) {
				musicPlaylistRepository.delete(lists);
			}
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	public ListDto<MusicPlaylistDto> findByUser(String userId) {

		ListDto<MusicPlaylistDto> response = new ListDto<MusicPlaylistDto>(
				UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			response.setData(musicPlaylistConvert
					.covertToDto(musicPlaylistRepository.findByUser(new User(
							userId))));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	public PageDto<MusicPlaylistDto> findByUser(String userId,
			PageSize pageSize, SortDto sort) {

		if (StringUtils.isBlank(userId)) {
			return PageDto.EMPTY_PAGE();
		}

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return musicPlaylistConvert.covertToDto(musicPlaylistRepository
				.findByUser(
						new User(userId),
						new PageRequest(pageSize.getPage() - 1, pageSize
								.getLimit(), SortUtils.covertSortDto(sort))));
	}

	@Override
	public CommonResponseDto addPlaylist(String userId, List<Long> mediaIds) {
		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (mediaIds == null || mediaIds.isEmpty()) {
				throw new VerificationException("资源编号错误");
			}
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			User user = new User(userId);
			for (Long id : mediaIds) {
				Music music = new Music(id);
				List<MusicPlaylist> lists = musicPlaylistRepository
						.findByUserAndMusic(user, music);
				if (lists == null || lists.isEmpty()) {
					MusicPlaylist list = new MusicPlaylist();
					list.setUser(user);
					list.setMusic(music);
					this.musicPlaylistRepository.save(list);
				}
			}
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

}
