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

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * @author WMS
 * 
 */
public interface CacheManagerApi {

	/**
	 * 获取缓存堆
	 * 
	 * @param name
	 * @return
	 * @throws CacheException
	 */
	public abstract <K,V> Cache<K, V> getCache(String name)
			throws CacheException;

	
	/**
	 * 注销管理器
	 */
	public abstract void destroy() throws CacheException;
	
	public MemcachedClient getMemcachedClient();
}
