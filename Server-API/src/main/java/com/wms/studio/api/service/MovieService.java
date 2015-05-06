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
package com.wms.studio.api.service;

import java.util.Date;
import java.util.List;

import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.media.MovieDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;

/**
 * @author WMS
 * 
 */
public interface MovieService {

	CommonResponseDto addMovie(MovieDto movieDto);

	CommonResponseDto modifyMovie(String userId, MovieDto movieDto);

	CommonResponseDto deleteMovie(String userId, Long ID);

	CommonResponseDto modifyMovies(String userId, List<MovieDto> movies);

	CommonResponseDto deleteMovies(String userId, List<Long> ids);

	PageDto<MovieDto> findBy(String title, String author, String actor,
			String type, String userId, Boolean auditingStatu,
			Date startMadeDate, Date endMadeDate, PageSize pageSize,
			SortDto sortDto);

	CommonResponseDto checkMovies(String userId, List<Long> ids);

}
