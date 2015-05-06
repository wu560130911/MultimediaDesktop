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

package com.wms.studio.covert;

import org.springframework.stereotype.Component;

import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.entity.Music;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("musicConvert")
public class MusicConvert extends AbstractCovert<Music, MusicDto> {

	@Override
	public MusicDto covertToDto(Music e) {

		if (e == null) {
			return null;
		}
		MusicDto dto = new MusicDto();

		dto.setDescription(e.getDescription());
		dto.setDuration(e.getDuration());
		dto.setFilename(e.getFilename());
		dto.setId(e.getId());
		dto.setSinger(e.getSinger());
		dto.setSize(e.getSize());
		dto.setTime(e.getTime());
		dto.setTitle(e.getTitle());
		dto.setType(e.getType());
		dto.setUserId(e.getUser().getId());
		dto.setYear(e.getYear());

		return dto;
	}

	@Override
	public Music covertToEntity(MusicDto d) throws VerificationException {

		if (d == null) {
			return null;
		}

		Music music = new Music();

		music.setDescription(d.getDescription());
		music.setDuration(d.getDuration());
		music.setFilename(d.getFilename());
		music.setId(d.getId());
		music.setSinger(d.getSinger());
		music.setSize(d.getSize());
		music.setTime(d.getTime());
		music.setTitle(d.getTitle());
		music.setType(d.getType());
		music.setUser(new User(d.getUserId()));
		music.setYear(d.getYear());

		return music;
	}

}
