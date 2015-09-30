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
package com.wms.studio.domain.transfer.sort;

import com.wms.studio.api.dto.sort.SortDto.Direction;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class OrderDto implements Serializable {

	private static final boolean DEFAULT_IGNORE_CASE = false;
	private Direction direction;
	private String property;
	private boolean ignoreCase;

	public OrderDto(Direction direction, String property) {
		this(direction, property, DEFAULT_IGNORE_CASE);
	}

	public OrderDto(Direction direction, String property, boolean ignoreCase) {
		this.direction = direction;
		this.property = property;
		this.ignoreCase = ignoreCase;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getProperty() {
		return property;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
