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
import javax.servlet.http.HttpServletRequest;

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
import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.service.MovieService;
import com.wms.studio.async.MovieAsyncCallable;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
@Controller
public class MovieController {

	private static final Logger log = Logger.getLogger(MovieController.class);

	@Resource
	private MovieService movieService;

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

	public void setMovieService(MovieService movieService) {
		this.movieService = movieService;
	}

	/**
	 * 异步处理文件上传，Servlet3规范新特性，不会消耗更多的资源
	 * 
	 * @param model
	 * @param movieFile
	 * @param movieDto
	 * @param madeDate
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/media/movie/add", method = RequestMethod.POST)
	public WebAsyncTask<Void> addMovie(Model model, MultipartFile movieFile,
			MovieDto movieDto,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date madeDate,
			HttpServletRequest request) {

		log.info("用户[" + UserUtils.getCurrentUserId() + "]正在上传视频[标题:"+movieDto.getTitle()+"]");

		return new WebAsyncTask<Void>(fileUploadTime, new MovieAsyncCallable(model,
				movieFile, movieDto, madeDate, movieService, filePath));
	}

	@RequestMapping(value = "/media/movie/list")
	public void listMovie(Model model, PageSize pageSize, String userId,
			String type, String actor, String title,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		PageDto<MovieDto> movies = movieService.findBy(title, null, actor,
				type, userId, Boolean.TRUE, startDate, endDate, pageSize, null);

		model.addAttribute("movies", movies.getValues());
		model.addAttribute("total", movies.getTotalElements());
	}

}
