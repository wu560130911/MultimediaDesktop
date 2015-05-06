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
package com.wms.studio.cache.lock;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import com.wms.studio.lock.LocalZookeeperLock;

/**
 * 
 * 在写时，采用分布式锁和缓存key对应的内部锁实现单个应用和集群一个时刻只有一个进行修改
 * @author WMS
 * 
 */
@SuppressWarnings("serial")
public class SyncLockMapCache<K, V> implements Cache<K, V>, Serializable {

	private static final Logger log = Logger.getLogger(SyncLockMapCache.class);
	private final String name;// MemCache缓存key
	private final MemcachedClient memcachedClient;
	private final LocalZookeeperLock lock;

	public SyncLockMapCache(String name, MemcachedClient memcachedClient,
			LocalZookeeperLock lock) throws CacheException {
		if (StringUtils.isBlank(name))
			throw new CacheException("缓存key不允许为空.");
		this.name = name;
		this.memcachedClient = memcachedClient;
		this.lock = lock;
	}

	// 采取读不加锁，但是写要进行加锁的原则
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
	public V put(K key, V value) throws CacheException {

		// 缓存名称一样的要保持一样的锁
		try {
			lock.lock();
			HashMap<K, V> attributes = memcachedClient.get(name);
			boolean isInit = false;
			if (attributes == null) {
				isInit = true;
				attributes = new HashMap<K, V>();
			}
			attributes.put(key, value);
			if (isInit) {
				memcachedClient.set(name, 0, attributes);
			} else {
				memcachedClient.replace(name, 0, attributes);
			}
			return value;
		} catch (Exception e) {
			log.fatal("写入MemCache缓存失败,请检查错误.", e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	@Override
	public V remove(K key) throws CacheException {

		// 缓存名称一样的要保持一样的锁
		try {
			lock.lock();
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes == null) {
				return null;
			}
			V value = attributes.remove(key);
			memcachedClient.replace(name, 0, attributes);
			return value;
		} catch (Exception e) {
			log.fatal("写入MemCache缓存失败,请检查错误.", e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	@Override
	public void clear() throws CacheException {
		// 缓存名称一样的要保持一样的锁
		try {
			lock.lock();
			memcachedClient.delete(name);
		} catch (Exception e) {
			log.fatal("写入MemCache缓存失败,请检查错误.", e);
		} finally {
			lock.unlock();
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

	public String toString() {
		return (new StringBuilder("SyncLockMapCache '")).append(name)
				.append("'").toString();
	}

	/**
	 * 批量缓存
	 * 
	 * @param cacheValues
	 */
	public void puts(Map<K, V> cacheValues) {
		// 缓存名称一样的要保持一样的锁
		try {
			lock.lock();
			HashMap<K, V> attributes = memcachedClient.get(name);
			boolean isInit = false;
			if (attributes == null) {
				isInit = true;
				attributes = new HashMap<K, V>();
			}
			attributes.putAll(cacheValues);
			if (isInit) {
				memcachedClient.set(name, 0, attributes);
			} else {
				memcachedClient.replace(name, 0, attributes);
			}
		} catch (Exception e) {
			log.fatal("写入MemCache缓存失败,请检查错误.", e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 批量删除缓存
	 * 
	 * @param cacheKey
	 */
	public void removes(List<K> cacheKey) {
		// 缓存名称一样的要保持一样的锁
		try {
			lock.lock();
			HashMap<K, V> attributes = memcachedClient.get(name);
			if (attributes == null) {
				return;
			}
			for (K key : cacheKey) {
				attributes.remove(key);
			}
			memcachedClient.replace(name, 0, attributes);
		} catch (Exception e) {
			log.fatal("写入MemCache缓存失败,请检查错误.", e);
		} finally {
			lock.unlock();
		}
	}
}
