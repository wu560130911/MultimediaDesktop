/**
 *
 */
package com.wms.studio.cache.redis;


import com.wms.studio.cache.api.Cache;
import com.wms.studio.cache.api.CacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hzwumsh
 *
 */
@SuppressWarnings("unchecked")
public class RedisCacheManager implements CacheManager {

	/**
	 * 私有的内部日志实例
	 */
	private static final Logger log = LogManager.getLogger(RedisCacheManager.class);

	// fast lookup by name map
	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	/*
	 * @see com.netease.hz.cache.CacheManager#getCache(java.lang.String)
	 */
	@Override
	public <K, V> Cache<K, V> getCache(String name) {
		log.debug("获取名称为: " + name + " 的RedisCache实例");

		Cache<K, V> c = caches.get(name);

		if (c == null) {

			// create a new cache instance
			c = new RedisCache<K, V>(name);

			// add it to the cache collection
			caches.put(name, c);
		}

		return c;
	}

}
