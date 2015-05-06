/**
 * 
 */
package com.wms.studio.async;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.api.service.MovieService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.FileUtils;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
public class MovieAsyncCallable implements Callable<Void> {

	private static final Logger log = Logger
			.getLogger(MovieAsyncCallable.class);

	private final Model model;
	private final MultipartFile movieFile;
	private final MovieDto movieDto;
	private final Date madeDate;
	private final MovieService movieService;
	private final String filePath;

	public MovieAsyncCallable(Model model, MultipartFile movieFile,
			MovieDto movieDto, Date madeDate, MovieService movieService,
			String filePath) {
		this.madeDate = madeDate;
		this.model = model;
		this.movieDto = movieDto;
		this.movieFile = movieFile;
		this.movieService = movieService;
		this.filePath = filePath;
	}

	@Override
	public Void call() throws Exception {

		try {

			if (movieFile == null || movieFile.isEmpty() || movieDto == null
					|| madeDate == null) {
				throw new VerificationException("参数错误");
			}

			File movie = FileUtils.getMovieFile(filePath,
					movieFile.getOriginalFilename());
			StringUtils.isValidObject(movie, "文件格式错误或系统错误");
			try {
				movieFile.transferTo(movie);
			} catch (IllegalStateException | IOException e) {
				log.fatal("写入文件失败，请检查", e);
				throw new VerificationException("写入文件错误");
			}
			movieDto.setFilename(movie.getName());
			movieDto.setUserId(UserUtils.getCurrentUserId());
			movieDto.setMadetime(madeDate);
			movieDto.setSize(movieFile.getSize());
			CommonResponseDto response = movieService.addMovie(movieDto);

			if (response == null
					|| UserConstant.SUCCESS != response.getResult()) {
				throw new VerificationException(response.getErrorMessage());
			} else {
				model.addAttribute("success", true);
			}

		} catch (VerificationException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("success", false);
		}

		return null;
	}

}
