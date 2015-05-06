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

package com.wms.studio.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.wms.studio.api.dto.sort.OrderDto;
import com.wms.studio.api.dto.sort.SortDto;

/**
 * @author WMS
 * 
 */
public class SortUtils {

	public static Sort covertSortDto(SortDto sortDto) {

		if (sortDto == null || sortDto.getOrders() == null
				|| sortDto.getOrders().isEmpty()) {
			return null;
		}
		List<Order> orders = new ArrayList<>();

		for (OrderDto orderdto : sortDto.getOrders()) {
			Order order = new Order(Sort.Direction.fromString(orderdto
					.getDirection().name()), orderdto.getProperty());
			if (orderdto.isIgnoreCase()) {
				order = order.ignoreCase();
			}
			orders.add(order);
		}

		return new Sort(orders);
	}
}
