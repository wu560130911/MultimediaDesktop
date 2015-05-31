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

import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.api.dto.media.MoviePlaylistDto;
import com.wms.studio.entity.Movie;
import com.wms.studio.entity.MoviePlaylist;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("moviePlaylistConvert")
public final class MoviePlaylistConvert extends
		AbstractCovert<MoviePlaylist, MoviePlaylistDto> {

	@Resource()
	@Qualifier("movieConvert")
	private EntityConvertInterface<Movie, MovieDto> movieCovert;

	public void setMovieCovert(
			EntityConvertInterface<Movie, MovieDto> movieCovert) {
		this.movieCovert = movieCovert;
	}

	@Override
	public MoviePlaylistDto covertToDto(MoviePlaylist e) {

		if (e == null) {
			return null;
		}

		MoviePlaylistDto dto = new MoviePlaylistDto();
		dto.setAddDate(e.getAddDate());
		dto.setId(e.getId());
		dto.setMovie(movieCovert.covertToDto(e.getMovie()));
		dto.setScore(e.getScore());

		return dto;
	}

	@Override
	public MoviePlaylist covertToEntity(MoviePlaylistDto d)
			throws VerificationException {

		if (d == null) {
			return null;
		}

		MoviePlaylist movie = new MoviePlaylist();
		movie.setMovie(new Movie(d.getMovie().getId()));
		movie.setScore(d.getScore());

		return movie;
	}

}
