package com.wms.studio.cache.api;

/**
 * 缓存管理接口，获取指定名称下的缓存Cache
 *
 * @author hzwumsh
 *
 */
public interface CacheManager {

	/**
	 * 获取指定名称的缓存，当这个缓存不存在时，将会创建一个缓存并返回。
	 *
	 * @param name
	 *            需要获取缓存的名称
	 * @return 指定名称的缓存
	 */
	public <K, V> Cache<K, V> getCache(String name);
}
