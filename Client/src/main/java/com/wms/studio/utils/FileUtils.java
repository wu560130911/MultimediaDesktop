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
package com.wms.studio.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wms.studio.api.utils.StringUtils;

public class FileUtils {

	private static final int MAXVALUE = 1 << 31;
	private static final Logger log = Logger.getLogger(FileUtils.class);

	private static final String FILE_MOVIE_PATH = "/movies/";
	private static final String FILE_MUSIC_PATH = "/musics/";
	private static final String FILE_SPLIT = "_";

	@SuppressWarnings("serial")
	private static final List<String> IMAGE_EXT = new ArrayList<String>() {
		{
			add(".png");
			add(".gif");
			add(".jpg");
			add(".jpeg");
		}
	};

	@SuppressWarnings("serial")
	private static final List<String> MOVIE_EXT = new ArrayList<String>() {
		{
			add(".mp4");
		}
	};

	@SuppressWarnings("serial")
	private static final List<String> MUSIC_EXT = new ArrayList<String>() {
		{
			add(".mp3");
			add(".ogg");
		}
	};

	public static String getRandomFileName() {
		String timeString = DateUtils.getDateFormat(DateUtils.YYYYMMDDHHIISSMS,
				new Date());
		Long Id = SpringContextHelper.getIncrNum();
		if (Id == null || Id == -1L) {
			Id = (long) (Math.random() * MAXVALUE);
		}
		return timeString + FILE_SPLIT + Id + FILE_SPLIT;
	}

	public static File getFile(String dir, String fileExt) {
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				log.fatal("创建目录失败，请检查:" + dir);
				return null;
			}
		}
		try {
			// 这儿包含重试机制，只要返回了File，就一定是不冲突的文件名
			return File.createTempFile(getRandomFileName(), fileExt, dirFile);
		} catch (IOException e) {
			log.fatal("创建文件失败", e);
			return null;
		}
	}

	public static File getImageFile(String dir, String sourcesFileName) {
		return getFile(dir, sourcesFileName, IMAGE_EXT);
	}

	public static File getMovieFile(String dir, String sourcesFileName) {

		return getFile(dir + FILE_MOVIE_PATH, sourcesFileName, MOVIE_EXT);
	}

	public static File getMusicFile(String dir, String sourcesFileName) {

		return getFile(dir + FILE_MUSIC_PATH, sourcesFileName, MUSIC_EXT);
	}

	private static File getFile(String dir, String sourcesFileName,
			List<String> exts) {
		if (StringUtils.isBlank(dir) || StringUtils.isBlank(sourcesFileName)
				|| exts == null || exts.isEmpty()) {
			return null;
		}
		int index = sourcesFileName.lastIndexOf(".");
		if (index == -1) {
			return null;
		}
		String fileExt = sourcesFileName.substring(index);
		if (!exts.contains(fileExt.toLowerCase())) {
			return null;
		}
		return getFile(dir, fileExt);
	}
}
