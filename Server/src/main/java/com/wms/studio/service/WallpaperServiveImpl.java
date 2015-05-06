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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.WallpaperEnum;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.WallpaperService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.User;
import com.wms.studio.entity.Wallpaper;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.repository.WallpaperRepository;

/**
 * 
 * @author WMS
 * 
 */
@Service("wallpaperService")
public class WallpaperServiveImpl implements WallpaperService {

	private static final Logger log = Logger
			.getLogger(WallpaperServiveImpl.class);
	@Autowired
	private WallpaperRepository wallpaperRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("wallpaperCovert")
	private EntityConvertInterface<Wallpaper, WallpaperDto> wallpaperCovert;

	public void setWallpaperCovert(
			EntityConvertInterface<Wallpaper, WallpaperDto> wallpaperCovert) {
		this.wallpaperCovert = wallpaperCovert;
	}

	public void setWallpaperRepository(WallpaperRepository wallpaperRepository) {
		this.wallpaperRepository = wallpaperRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	@HandlerPoint(handlerName = "wallpaperAddHandler")
	public CommonResponseDto addWallpaper(WallpaperDto wallpaperDto) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {
			Wallpaper wallpaper = wallpaperCovert.covertToEntity(wallpaperDto);

			if (wallpaper == null) {
				log.error("转换Wallpaper错误，转换为空，请检查参数");
				throw new VerificationException("参数错误");
			}

			if (StringUtils.isBlank(wallpaper.getPath())
					|| wallpaper.getUser() == null
					|| StringUtils.isBlank(wallpaper.getUser().getId())
					|| wallpaper.getWallpaperType() == null) {
				log.error("校验参数错误，请检查参数:"
						+ ToStringBuilder.reflectionToString(wallpaper));
				throw new VerificationException("参数错误");
			}

			if (this.userRepository.exists(wallpaper.getUser().getId())) {

				this.wallpaperRepository.save(wallpaper);
				responseDto.setResult(UserConstant.SUCCESS);

			} else {
				log.fatal("添加壁纸用户不存在."
						+ ToStringBuilder.reflectionToString(wallpaper));
				throw new VerificationException("参数错误");
			}

		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Override
	@Transactional
	public CommonResponseDto deleteWallpaper(Integer id) {
		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {
			if (id == null) {
				log.error("删除壁纸ID不允许为空");
				throw new VerificationException("参数错误");
			}

			if (this.wallpaperRepository.exists(id)) {

				this.wallpaperRepository.delete(id);
				responseDto.setResult(UserConstant.SUCCESS);

			} else {
				log.fatal("删除壁纸ID不存在.");
				throw new VerificationException("参数错误");
			}

		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<WallpaperDto> findBy(final WallpaperEnum wallpaperType,
			final Date startDate, final Date endDate, PageSize pageSize) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		Page<Wallpaper> pageWallpaper = this.wallpaperRepository.findAll(
				new Specification<Wallpaper>() {

					@Override
					public Predicate toPredicate(Root<Wallpaper> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {

						List<Predicate> pres = new ArrayList<Predicate>();

						if (wallpaperType != null) {
							pres.add(cb
									.equal(root.get("wallpaperType").as(
											WallpaperEnum.class), wallpaperType));
						}

						if (startDate != null) {
							pres.add(cb.greaterThanOrEqualTo(root
									.get("addDate").as(Date.class), startDate));
						}

						if (endDate != null) {
							pres.add(cb.lessThanOrEqualTo(root.get("addDate")
									.as(Date.class), endDate));
						}

						Predicate[] p = new Predicate[pres.size()];
						return cb.and(pres.toArray(p));
					}
				}, new PageRequest(pageSize.getPage() - 1, pageSize.getLimit()));

		return this.wallpaperCovert.covertToDto(pageWallpaper);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<WallpaperDto> findByUser(String userId, PageSize pageSize) {

		if (StringUtils.isBlank(userId)) {
			return PageDto.EMPTY_PAGE();
		}

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return this.wallpaperCovert.covertToDto(this.wallpaperRepository
				.findByUser(
						new User(userId),
						new PageRequest(pageSize.getPage() - 1, pageSize
								.getLimit())));
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public PageDto<WallpaperDto> listWallpapers(PageSize pageSize) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return this.wallpaperCovert.covertToDto(wallpaperRepository
				.findAll(new PageRequest(pageSize.getPage() - 1, pageSize
						.getLimit())));
	}

}
