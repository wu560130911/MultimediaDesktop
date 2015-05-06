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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class MemCacheMapCache<K, V> implements Cache<K, V>, Serializable {

	private static final Logger log = Logger.getLogger(MemCacheMapCache.class);
	private static final int reCount = 5;
	private final String name;// MemCache缓存key
	private final MemcachedClient memcachedClient;

	public MemCacheMapCache(String name, MemcachedClient memcachedClient)
			throws CacheException {
		if (StringUtils.isBlank(name))
			throw new CacheException("缓存key不允许为空.");
		if (memcachedClient == null) {
			throw new CacheException("memcachedClient不允许为空.");
		}

		this.memcachedClient = memcachedClient;
		this.name = name;
	}

	@Override
	public V get(K key) throws CacheException {
		try {
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes != null) {
				return attributes.get(key);
			}
		} catch (Exception e) {
			log.fatal("获取MemCache缓存失败,请检查错误.", e);
		}
		return null;
	}

	@Override
	public V put(final K key, final V value) throws CacheException {

		// 这儿同一个缓存的对象进行加锁，使得每台机器只有一个线程可以对memcache进行操作，然后使用重试机制，使得集群可以同时进行修改
		synchronized (name) {
			try {
				// 因为MemCache存在bug，所以这儿先进行校验
				GetsResponse<V> response = memcachedClient.gets(name);

				if (response == null) {
					memcachedClient.add(name, 0, new HashMap<>());
				}

				memcachedClient.cas(name, new CASOperation<HashMap<K, V>>() {

					@Override
					public int getMaxTries() {
						return reCount;
					}

					@Override
					public HashMap<K, V> getNewValue(long currentCAS,
							HashMap<K, V> currentValue) {

						if (currentValue == null) {
							currentValue = new HashMap<>();
						}
						currentValue.put(key, value);
						return currentValue;
					}
				});
			} catch (TimeoutException | InterruptedException
					| MemcachedException e) {
				log.fatal("MemCache缓存更新失败", e);
				return null;
			}
		}

		return value;
	}

	@Override
	public V remove(final K key) throws CacheException {
		// 这儿同一个缓存的对象进行加锁，使得每台机器只有一个线程可以对memcache进行操作，然后使用重试机制，使得集群可以同时进行修改
		synchronized (name) {
			try {
				memcachedClient.cas(name, new CASOperation<HashMap<K, V>>() {

					@Override
					public int getMaxTries() {
						return reCount;
					}

					@Override
					public HashMap<K, V> getNewValue(long currentCAS,
							HashMap<K, V> currentValue) {

						if (currentValue == null) {
							currentValue = new HashMap<>();
						}
						currentValue.remove(key);
						return currentValue;
					}
				});
			} catch (TimeoutException | InterruptedException
					| MemcachedException e) {
				log.fatal("MemCache缓存更新失败", e);
			}
		}

		return null;
	}

	@Override
	public void clear() throws CacheException {
		// 这儿同一个缓存的对象进行加锁，使得每台机器只有一个线程可以对memcache进行操作，然后使用重试机制，使得集群可以同时进行修改
		synchronized (name) {
			try {
				memcachedClient.cas(name, new CASOperation<HashMap<K, V>>() {

					@Override
					public int getMaxTries() {
						return reCount;
					}

					@Override
					public HashMap<K, V> getNewValue(long currentCAS,
							HashMap<K, V> currentValue) {

						if (currentValue == null) {
							currentValue = new HashMap<>();
						}
						currentValue.clear();
						return currentValue;
					}
				});
			} catch (TimeoutException | InterruptedException
					| MemcachedException e) {
				log.fatal("MemCache缓存更新失败", e);
			}
		}
	}

	@Override
	public int size() {
		try {
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes != null) {
				return attributes.size();
			}
		} catch (Exception e) {
			log.fatal("获取MemCache缓存失败,请检查错误.", e);
		}
		return 0;
	}

	@Override
	public Set<K> keys() {
		try {
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes != null) {
				Set<K> keys = attributes.keySet();
				if (!keys.isEmpty())
					return Collections.unmodifiableSet(keys);
			}
		} catch (Exception e) {
			log.fatal("获取MemCache缓存失败,请检查错误.", e);
		}
		return Collections.emptySet();
	}

	@Override
	public Collection<V> values() {
		try {
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes != null) {
				Collection<V> values = attributes.values();
				if (!CollectionUtils.isEmpty(values))
					return Collections.unmodifiableCollection(values);
			}
		} catch (Exception e) {
			log.fatal("获取MemCache缓存失败,请检查错误.", e);
		}
		return Collections.emptySet();
	}

}
