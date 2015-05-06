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
package com.wms.studio.api.dto.sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WMS
 *
 */
@SuppressWarnings("serial")
public class SortDto implements Serializable{

	private List<OrderDto> orders;
	
	public SortDto(List<OrderDto> orders) {
		this.orders = orders;
	}
	
	public SortDto(OrderDto order) {
		this.orders = new ArrayList<>(1);
		this.orders.add(order);
	}
	
	public List<OrderDto> getOrders() {
		return orders;
	}
	
	public static enum Direction {

		ASC, DESC;
	}
	
	public SortDto(Direction direction, String... properties) {
		this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
	}
	
	public SortDto(Direction direction, List<String> properties) {

		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("You have to provide at least one property to sort by!");
		}

		this.orders = new ArrayList<OrderDto>(properties.size());

		for (String property : properties) {
			this.orders.add(new OrderDto(direction, property));
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

