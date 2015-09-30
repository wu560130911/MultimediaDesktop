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
package com.wms.studio.service.api;


import com.wms.studio.domain.transfer.CommonResponseDto;
import com.wms.studio.domain.transfer.PageDto;
import com.wms.studio.domain.transfer.media.MusicDto;
import com.wms.studio.domain.transfer.page.PageSize;
import com.wms.studio.domain.transfer.sort.SortDto;

import java.util.Date;
import java.util.List;

/**
 * @author WMS
 * 
 */
public interface MusicService {

	CommonResponseDto addMusic(MusicDto MusicDto);

	CommonResponseDto modifyMusic(String userId, MusicDto MusicDto);

	CommonResponseDto deleteMusic(String userId, Long ID);

	CommonResponseDto modifyMusics(String userId, List<MusicDto> Musics);

	CommonResponseDto deleteMusics(String userId, List<Long> ids);

	PageDto<MusicDto> findBy(String title, String singer, String type,
							 String userId, Boolean auditingStatu, Date startMadeDate,
							 Date endMadeDate, PageSize pageSize, SortDto sortDto);

	CommonResponseDto checkMusics(String userId, List<Long> ids);

}
