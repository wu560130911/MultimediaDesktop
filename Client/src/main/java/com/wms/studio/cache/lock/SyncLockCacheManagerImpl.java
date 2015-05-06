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

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.wms.studio.cache.CacheManagerApi;
import com.wms.studio.lock.LocalZookeeperLock;

import static com.wms.studio.lock.LocalZookeeperLock.isContainJvmLock;

/**
 * MemCache缓存管理，本实现在系统内部对于同一个缓存key采用同一个com.wms.studio.lock.LocalZookeeperLock.jvmLock，也就是同一个缓存在一个时刻只允许一个线程去获取分布式写锁，然后进行写入操作。
 * 所以存在两个锁机制。本实现依赖于缓存类SyncLockMapCache.
 * @author WMS
 *
 */
public class SyncLockCacheManagerImpl implements CacheManagerApi {

	private static final Logger log = Logger
			.getLogger(SyncLockCacheManagerImpl.class);
	private static final String LOCK_ROOT_PATH = "/lock/"
			+ CacheManagerApi.class.getName() + "/";

	private MemcachedClient memcachedClient;

	private ZooKeeper zooKeeper;

	private String zookeeperAddress;

	private int timeout;

	public SyncLockCacheManagerImpl() {

	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setZookeeperAddress(String zookeeperAddress) {
		this.zookeeperAddress = zookeeperAddress;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {

		return getMemCache(name);
	}

	@Override
	public void destroy() throws CacheException {
		try {
			memcachedClient.shutdown();
			zooKeeper.close();
		} catch (Exception e) {
			log.fatal("memcachedClient关闭异常", e);
			throw new CacheException(e);
		}
	}

	/**
	 * 这儿不对MemCache是否存在缓存进行判断，全部交给{@link SyncLockMapCache UserMapCache}进行处理
	 * 
	 * @param name
	 * @return
	 * @throws CacheException
	 */
	public <K, V> SyncLockMapCache<K, V> getMemCache(String name)
			throws CacheException {

		if (isContainJvmLock(name)) {
			return getMemSyncCache(name);
		}

		synchronized (name) {
			return getMemSyncCache(name);
		}
	}

	private <K, V> SyncLockMapCache<K, V> getMemSyncCache(String name)
			throws CacheException {
		try {
			return new SyncLockMapCache<K, V>(name, memcachedClient,
					new LocalZookeeperLock(zooKeeper, LOCK_ROOT_PATH, name));
		} catch (Exception e) {
			log.fatal("用户登录缓存获取失败,缓存信息[name=" + name + "]", e);
			throw new CacheException(e);
		}
	}

	public void initZooKeeper() {
		try {
			zooKeeper = new ZooKeeper(zookeeperAddress, timeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
				}
			});
		} catch (IOException e) {
		}
	}
}
