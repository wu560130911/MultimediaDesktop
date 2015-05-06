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

package com.wms.studio.cache.memcache;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.wms.studio.cache.CacheManagerApi;

/**
 * 使用MemCache内部的锁机制实现操作的原子性
 * 
 * @author WMS
 * 
 */
public class MemCacheManagerImpl implements CacheManagerApi {

	private static final Logger log = Logger
			.getLogger(MemCacheManagerImpl.class);

	private MemcachedClient memcachedClient;

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {

		return getMemCacheMapCache(name);
	}

	@Override
	public void destroy() throws CacheException {
		try {
			memcachedClient.shutdown();
		} catch (Exception e) {
			log.fatal("memcachedClient关闭异常", e);
			throw new CacheException(e);
		}
	}

	public <K, V> MemCacheMapCache<K, V> getMemCacheMapCache(String name)
			throws CacheException {
		return new MemCacheMapCache<>(name, memcachedClient);
	}

	@Override
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

}
