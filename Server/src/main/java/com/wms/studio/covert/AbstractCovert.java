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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

import com.wms.studio.api.dto.PageDto;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
public abstract class AbstractCovert<Entity, Dto> implements
		EntityConvertInterface<Entity, Dto> {

	@Override
	public List<Dto> covertToDto(List<Entity> es) {

		if (es == null || es.isEmpty()) {
			return Collections.emptyList();
		}

		List<Dto> dtos = new ArrayList<>(es.size());
		for (Entity e : es) {
			Dto dto = covertToDto(e);
			if (dto != null) {
				dtos.add(dto);
			}
		}

		return dtos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageDto<Dto> covertToDto(Page<Entity> es) {

		if (es == null) {
			return new PageDto<Dto>(Collections.EMPTY_LIST, 0, 0);
		}

		return new PageDto<Dto>(covertToDto(es.getContent()),
				es.getTotalPages(), es.getTotalElements());
	}

	@Override
	public List<Entity> covertToEntity(List<Dto> ds) throws VerificationException {

		if (ds == null || ds.isEmpty()) {
			return Collections.emptyList();
		}

		List<Entity> es = new ArrayList<Entity>(ds.size());
		for (Dto dto : ds) {
			Entity e = covertToEntity(dto);
			if (e != null) {
				es.add(e);
			}
		}

		return es;
	}

}
