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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wms.studio.api.dto.media.MusicDto;
import com.wms.studio.api.dto.media.MusicPlaylistDto;
import com.wms.studio.entity.Music;
import com.wms.studio.entity.MusicPlaylist;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 *
 */
@Component("musicPlaylistConvert")
public class MusicPlaylistConvert extends AbstractCovert<MusicPlaylist, MusicPlaylistDto> {

	@Resource()
	@Qualifier("musicConvert")
	private EntityConvertInterface<Music, MusicDto> musicCovert;

	public void setMusicCovert(
			EntityConvertInterface<Music, MusicDto> musicCovert) {
		this.musicCovert = musicCovert;
	}
	
	@Override
	public MusicPlaylistDto covertToDto(MusicPlaylist e) {

		if(e==null){
			return null;
		}
		MusicPlaylistDto dto = new MusicPlaylistDto();
		
		dto.setAddDate(e.getAddDate());
		dto.setId(e.getId());
		dto.setMusic(musicCovert.covertToDto(e.getMusic()));
		dto.setScore(e.getScore());
		
		return dto;
	}

	@Override
	public MusicPlaylist covertToEntity(MusicPlaylistDto d)
			throws VerificationException {

		if(d==null){
			return null;
		}
		MusicPlaylist music = new MusicPlaylist();
		music.setMusic(new Music(d.getMusic().getId()));
		music.setScore(d.getScore());
		return music;
	}

}
