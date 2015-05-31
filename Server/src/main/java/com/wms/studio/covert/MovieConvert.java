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

import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.entity.Movie;
import com.wms.studio.entity.User;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Component("movieConvert")
public final class MovieConvert extends AbstractCovert<Movie, MovieDto> {

	@Override
	public MovieDto covertToDto(Movie e) {

		if (e == null) {
			return null;
		}

		MovieDto dto = new MovieDto();

		dto.setActor(e.getActor());
		dto.setAuthor(e.getAuthor());
		dto.setDescription(e.getDescription());
		dto.setDuration(e.getDuration());
		dto.setFilename(e.getFilename());
		dto.setId(e.getId());
		dto.setMadetime(e.getMadetime());
		dto.setSize(e.getSize());
		dto.setTime(e.getTime());
		dto.setTitle(e.getTitle());
		dto.setType(e.getType());
		dto.setUserId(e.getUser().getId());

		return dto;
	}

	@Override
	public Movie covertToEntity(MovieDto d) throws VerificationException {

		if (d == null) {
			return null;
		}

		Movie movie = new Movie();

		movie.setActor(d.getActor());
		movie.setAuthor(d.getAuthor());
		movie.setDescription(d.getDescription());
		movie.setDuration(d.getDuration());
		movie.setFilename(d.getFilename());
		movie.setId(d.getId());
		movie.setMadetime(d.getMadetime());
		movie.setSize(d.getSize());
		movie.setTime(d.getTime());
		movie.setTitle(d.getTitle());
		movie.setType(d.getType());
		movie.setUser(new User(d.getUserId()));

		return movie;
	}

}
