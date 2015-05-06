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

package com.wms.studio.service.handler.api;

import java.util.HashMap;

/**
 * @author WMS
 * 
 */
public class HandlerData {

	private HashMap<String, Object> hashMap = new HashMap<>(2);
	private static final String ARGS_KEY = "args";
	private static final String RESULT_KEY = "result";

	public HandlerData() {

	}

	public Object setArgs(Object object) {
		return hashMap.put(ARGS_KEY, object);
	}

	public Object setResult(Object object) {
		return hashMap.put(RESULT_KEY, object);
	}

	@SuppressWarnings("unchecked")
	public <V> V getArgs() {
		return (V) hashMap.get(ARGS_KEY);
	}

	@SuppressWarnings("unchecked")
	public <V> V getResult() {
		return (V) hashMap.get(RESULT_KEY);
	}

	public Object put(String key, Object object) {
		return hashMap.put(key, object);
	}

	public Object getObject(String key) {
		return hashMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <V> V getValue(String key) {
		return (V) hashMap.get(key);
	}
}
