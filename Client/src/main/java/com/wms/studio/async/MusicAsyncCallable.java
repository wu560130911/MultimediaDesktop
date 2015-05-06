/**
 * 
 */
package com.wms.studio.async;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.api.service.MusicService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.utils.FileUtils;
import com.wms.studio.utils.UserUtils;

/**
 * @author WMS
 * 
 */
public class MusicAsyncCallable implements Callable<Void> {

	private static final Logger log = Logger.getLogger(MusicAsyncCallable.class);
	
	private final Model model;
	private final MultipartFile musicFile;
	private final MusicDto musicDto;
	private final String filePath;
	private final MusicService musicService;

	public MusicAsyncCallable(Model model, MultipartFile musicFile,
			MusicDto musicDto, String filePath, MusicService musicService) {
		this.model = model;
		this.musicDto = musicDto;
		this.musicFile = musicFile;
		this.filePath = filePath;
		this.musicService = musicService;
	}

	@Override
	public Void call() throws Exception {

		try {

			if (musicFile == null || musicFile.isEmpty() || musicDto == null
					|| musicDto == null) {
				throw new VerificationException("参数错误");
			}

			File music = FileUtils.getMusicFile(filePath,
					musicFile.getOriginalFilename());
			StringUtils.isValidObject(music, "文件格式错误或系统错误");
			try {
				musicFile.transferTo(music);
			} catch (IllegalStateException | IOException e) {
				log.fatal("写入文件失败，请检查", e);
				throw new VerificationException("写入文件错误");
			}
			musicDto.setFilename(music.getName());
			musicDto.setUserId(UserUtils.getCurrentUserId());
			musicDto.setSize(musicFile.getSize());
			CommonResponseDto response = musicService.addMusic(musicDto);

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
