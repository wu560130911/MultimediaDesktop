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

package com.wms.studio.service.handler.aop;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.service.handler.api.HandlerApi;

/**
 * @author WMS
 * 
 */
public abstract class AbstractAopHandler<T> {

	private static final Logger log = Logger
			.getLogger(AbstractAopHandler.class);

	@Autowired
	private ApplicationContext context;

	private List<HandlerApi<T>> handlers = new ArrayList<HandlerApi<T>>();

	public abstract String getHandlersName();

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		if (context == null) {
			log.fatal("未获取到ApplicationContext错误.");
			throw new RuntimeException("未获取到ApplicationContext错误.");
		}

		if (StringUtils.isBlank(getHandlersName())) {
			log.info("未配置处理逻辑，请检查");
			return;
		}
		
		for (String handlerName : getHandlersName().split(",")) {
			if (StringUtils.isBlank(handlerName)) {
				log.info("配置处理逻辑表示不允许为空，请检查");
				continue;
			}
			HandlerApi<T> handler = context.getBean(handlerName,
					HandlerApi.class);
			if (handler == null) {
				log.fatal("请检查处理标识，未找到该处理类:" + handlerName);
				continue;
			}
			handlers.add(handler);
		}
	}

	protected void excuteHandler(T t) {
		for (HandlerApi<T> handler : handlers) {
			handler.excute(t);
		}
	}
}
