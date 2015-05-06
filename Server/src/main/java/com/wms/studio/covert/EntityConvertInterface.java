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

import java.util.List;

import org.springframework.data.domain.Page;

import com.wms.studio.api.dto.PageDto;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
public interface EntityConvertInterface<Entity, Dto> {

	Dto covertToDto(Entity e);

	List<Dto> covertToDto(List<Entity> es);
	
	PageDto<Dto> covertToDto(Page<Entity> es);

	Entity covertToEntity(Dto d) throws VerificationException;

	List<Entity> covertToEntity(List<Dto> ds) throws VerificationException;
}
