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
package com.wms.studio.cache;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
public class MemCacheManager implements CacheManager, Destroyable {

	private Logger log = Logger.getLogger(MemCacheManager.class);

	private CacheManagerApi cacheManager;

	public CacheManagerApi getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManagerApi cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void destroy() throws Exception {
		cacheManager.destroy();
	}

	@Override
	public final <K, V> Cache<K, V> getCache(String name)
			throws CacheException {

		if (this.cacheManager == null) {
			log.fatal("请配置缓存管理器");
			throw new CacheException("请配置缓存管理器");
		}

		return cacheManager.getCache(name);
	}

}
