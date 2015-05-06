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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.ListDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MoviePlaylistDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.service.MoviePlaylistService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Movie;
import com.wms.studio.entity.MoviePlaylist;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.MoviePlaylistRepository;
import com.wms.studio.repository.MovieRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.utils.SortUtils;

@Service("moviePlaylistService")
public class MoviePlaylistServiceImpl implements MoviePlaylistService {

	@Resource
	private MoviePlaylistRepository moviePlaylistRepository;

	@Resource
	private UserRepository userRepository;

	@Resource
	private MovieRepository movieRepository;

	@Resource()
	@Qualifier("moviePlaylistConvert")
	private EntityConvertInterface<MoviePlaylist, MoviePlaylistDto> moviePlaylistCovert;

	public void setMoviePlaylistCovert(
			EntityConvertInterface<MoviePlaylist, MoviePlaylistDto> moviePlaylistCovert) {
		this.moviePlaylistCovert = moviePlaylistCovert;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setMoviePlaylistRepository(
			MoviePlaylistRepository moviePlaylistRepository) {
		this.moviePlaylistRepository = moviePlaylistRepository;
	}

	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	@Transactional
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
			if (!movieRepository.exists(mediaId)) {
				throw new VerificationException("资源编号不存在");
			}

			User user = new User(userId);
			Movie movie = new Movie(mediaId);

			List<MoviePlaylist> lists = moviePlaylistRepository
					.findByUserAndMovie(user, movie);
			if (lists == null || lists.isEmpty()) {
				MoviePlaylist list = new MoviePlaylist();
				list.setUser(user);
				list.setMovie(movie);
				this.moviePlaylistRepository.save(list);
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
	@Transactional
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

			MoviePlaylist list = moviePlaylistRepository.findOne(id);

			if (list == null) {
				throw new VerificationException("资源编号错误");
			}

			if (list.getUser().getId().equals(userId)) {
				moviePlaylistRepository.delete(list);
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
	@Transactional
	public CommonResponseDto deletePlaylistByUser(String userId) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			List<MoviePlaylist> lists = moviePlaylistRepository
					.findByUser(new User(userId));

			if (lists != null && !lists.isEmpty()) {
				moviePlaylistRepository.delete(lists);
			}
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public ListDto<MoviePlaylistDto> findByUser(String userId) {

		ListDto<MoviePlaylistDto> response = new ListDto<MoviePlaylistDto>(
				UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "账号错误");
			if (!userRepository.exists(userId)) {
				throw new VerificationException("账号不存在");
			}

			List<MoviePlaylist> lists = moviePlaylistRepository
					.findByUser(new User(userId));

			response.setData(moviePlaylistCovert.covertToDto(lists));
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public PageDto<MoviePlaylistDto> findByUser(String userId,
			PageSize pageSize, SortDto sort) {

		if (StringUtils.isBlank(userId)) {
			return PageDto.EMPTY_PAGE();
		}

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return moviePlaylistCovert.covertToDto(moviePlaylistRepository
				.findByUser(
						new User(userId),
						new PageRequest(pageSize.getPage() - 1, pageSize
								.getLimit(), SortUtils.covertSortDto(sort))));
	}

	@Override
	@Transactional
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
				Movie movie = new Movie(id);
				List<MoviePlaylist> lists = moviePlaylistRepository
						.findByUserAndMovie(user, movie);
				if (lists == null || lists.isEmpty()) {
					MoviePlaylist list = new MoviePlaylist();
					list.setUser(user);
					list.setMovie(movie);
					this.moviePlaylistRepository.save(list);
				}
			}
			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

}
