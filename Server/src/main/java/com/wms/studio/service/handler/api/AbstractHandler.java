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

/**
 * 
 * @author WMS
 * 
 */
public abstract class AbstractHandler implements HandlerApi<HandlerData> {

	public abstract boolean beforeHandler(HandlerData data);

	public abstract boolean handler(HandlerData data);

	public abstract boolean afterHandler(HandlerData data);

	@Override
	public boolean excute(HandlerData data) {
		return beforeHandler(data) || handler(data) || afterHandler(data);
	}

	public Object[] getArgs(HandlerData data) {
		return data.getArgs();
	}

	@SuppressWarnings("unchecked")
	public <T> T getArgsObject(HandlerData data, Integer index) {
		Object[] objects = getArgs(data);
		if (objects == null || objects.length == 0) {
			return null;
		}
		if (index > objects.length) {
			return null;
		}
		return (T) objects[index];
	}

	public <T> T getResult(HandlerData data) {
		return data.getResult();
	}

	@Override
	public boolean isAbort() {
		return false;
	}
}
