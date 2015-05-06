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

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.shiro.cache.Cache;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.wms.studio.cache.CacheManagerApi;
import com.wms.studio.constant.SessionKeyConstant;

public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static MemcachedClient client;

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		this.client = getBean("memcachedClient", MemcachedClient.class);
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static <K, V> Cache<K, V> getMemcache(String name) {
		CacheManagerApi cacheManager = getBean("cacheManager",
				CacheManagerApi.class);
		if (cacheManager == null) {
			return null;
		}
		return cacheManager.getCache(name);
	}

	public static MemcachedClient getClient() {
		return client;
	}

	public static Long getIncrNum() {
		try {
			return client.incr(SessionKeyConstant.SYSTEM_INRC_KEY,
					SessionKeyConstant.DEFAULT_VALUE,
					SessionKeyConstant.DEFAULT_VALUE);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			return -1L;
		}
	}
}
