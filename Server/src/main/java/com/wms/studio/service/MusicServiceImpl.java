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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.service.MusicService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.Music;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.MusicRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.utils.SortUtils;

/**
 * 
 * @author WMS
 * 
 */
@Service("musicService")
public class MusicServiceImpl implements MusicService {

	@Resource
	private MusicRepository musicRepository;

	@Resource
	private UserRepository userRepository;

	@Resource()
	@Qualifier("musicConvert")
	private EntityConvertInterface<Music, MusicDto> musicCovert;

	public void setMusicCovert(
			EntityConvertInterface<Music, MusicDto> musicCovert) {
		this.musicCovert = musicCovert;
	}

	public void setMusicRepository(MusicRepository musicRepository) {
		this.musicRepository = musicRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public CommonResponseDto addMusic(MusicDto MusicDto) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			Music Music = musicCovert.covertToEntity(MusicDto);

			StringUtils.isValidObject(Music, "参数错误");
			StringUtils.isValidString(MusicDto.getUserId(), "上传用户不允许为空");
			StringUtils.isValidString(Music.getTitle(), "音乐名称不允许为空");
			StringUtils.isValidString(Music.getSinger(), "歌手不允许为空");
			StringUtils.isValidString(Music.getDescription(), "描述不允许为空");
			StringUtils.isValidString(Music.getFilename(), "文件名称不允许为空");
			StringUtils.isValidString(Music.getType(), "类型不允许为空");

			// User user = this.userRepository.findOne(MusicDto.getUserId());
			// StringUtils.isValidObject(user, "上传用户不允许为空");
			// Music.setUser(user);
			this.musicRepository.save(Music);

			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto modifyMusic(String userId, MusicDto MusicDto) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			Music Music = musicCovert.covertToEntity(MusicDto);
			StringUtils.isValidObject(Music, "参数错误");
			StringUtils.isValidString(MusicDto.getUserId(), "上传用户不允许为空");
			StringUtils.isValidString(Music.getTitle(), "音乐名称不允许为空");
			StringUtils.isValidString(Music.getSinger(), "歌手不允许为空");
			StringUtils.isValidString(Music.getDescription(), "描述不允许为空");
			StringUtils.isValidString(Music.getFilename(), "文件名称不允许为空");
			StringUtils.isValidString(Music.getType(), "类型不允许为空");

			if (Music.getId() <= 0 || !musicRepository.exists(Music.getId())) {
				throw new VerificationException("更新错误");
			}

			User user = this.userRepository.findOne(MusicDto.getUserId());
			StringUtils.isValidObject(user, "上传用户不允许为空");
			if (user.getRole().equals(UserRole.管理员)
					|| user.getId().equals(Music.getUser().getId())) {
				// 更新
				this.musicRepository.save(Music);
			} else {
				throw new VerificationException("没有权限更新");
			}

			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteMusic(String userId, Long ID) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			StringUtils.isValidString(userId, "操作用户不允许为空");
			StringUtils.isValidObject(ID, "资源编号不允许为空");

			if (ID <= 0 || !musicRepository.exists(ID)) {
				throw new VerificationException("更新错误");
			}

			Music Music = musicRepository.findOne(ID);

			User user = this.userRepository.findOne(userId);
			StringUtils.isValidObject(user, "上传用户不允许为空");

			if (user.getRole().equals(UserRole.管理员)
					|| user.getId().equals(Music.getUser().getId())) {
				this.musicRepository.delete(Music);
			} else {
				throw new VerificationException("没有权限更新");
			}

			response.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto modifyMusics(String userId, List<MusicDto> Musics) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		if (Musics == null) {
			return response;
		}

		for (MusicDto Music : Musics) {
			response = modifyMusic(userId, Music);
			if (UserConstant.SUCCESS != response.getResult()) {
				return response;
			}
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteMusics(String userId, List<Long> ids) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		if (ids == null) {
			return response;
		}

		for (Long id : ids) {
			response = deleteMusic(userId, id);
			if (UserConstant.SUCCESS != response.getResult()) {
				return response;
			}
		}

		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<MusicDto> findBy(final String title, final String singer,
			final String type, final String userId,
			final Boolean auditingStatu, final Date startMadeDate,
			final Date endMadeDate, PageSize pageSize, SortDto sortDto) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return musicCovert.covertToDto(musicRepository.findAll(
				new Specification<Music>() {

					@Override
					public Predicate toPredicate(Root<Music> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {

						List<Predicate> pres = new ArrayList<Predicate>();

						if (!StringUtils.isBlank(title)) {
							pres.add(cb.like(
									root.get("title").as(String.class), title));
						}

						if (!StringUtils.isBlank(singer)) {
							pres.add(cb.like(root.get("singer")
									.as(String.class), singer));
						}

						if (!StringUtils.isBlank(type)) {
							pres.add(cb.like(root.get("type").as(String.class),
									"%" + type + "%"));
						}

						if (!StringUtils.isBlank(userId)) {
							pres.add(cb.equal(root.get("user").as(User.class),
									new User(userId)));
						}

						// 没办法。Mysql不支持bit查询
						pres.add(cb.equal(
								root.get("auditingStatu").as(Integer.class),
								auditingStatu ? 1 : 0));

						if (startMadeDate != null) {
							pres.add(cb.greaterThanOrEqualTo(
									root.get("madetime").as(Date.class),
									startMadeDate));
						}

						if (endMadeDate != null) {
							pres.add(cb.lessThanOrEqualTo(root.get("madetime")
									.as(Date.class), endMadeDate));
						}

						Predicate[] p = new Predicate[pres.size()];
						return cb.and(pres.toArray(p));
					}
				}, new PageRequest(pageSize.getPage() - 1, pageSize.getLimit(),
						SortUtils.covertSortDto(sortDto))));
	}

	@Override
	@Transactional
	public CommonResponseDto checkMusics(String userId, List<Long> ids) {

		CommonResponseDto response = new CommonResponseDto(UserConstant.ERROR);

		try {

			// 参数校验
			StringUtils.isValidString(userId, "用户不为空");
			StringUtils.isValidObject(ids, "应用不为空");
			User user = userRepository.findOne(userId);
			StringUtils.isValidObject(user, "用户不为空");

			if (!UserRole.管理员.equals(user.getRole())) {
				throw new VerificationException("用户没有审核权限");
			}

			// 这儿会有批量提交，但是只有一个事务提交，所以如果存在异常，所有都不会有改动
			for (Long id : ids) {
				Music Music = musicRepository.findOne(id);
				if (Music != null && !Music.getAuditingStatu()) {
					Music.setAuditingStatu(Boolean.TRUE);
				}
			}

			response.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}

}
