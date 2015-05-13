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

package com.wms.studio.lock;

import static com.wms.studio.utils.AddressUtil.formatPath;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.WriteLock;

/**
 * @author WMS
 * 
 */
public class LocalZookeeperLock {

	private static final Logger LOG = Logger
			.getLogger(LocalZookeeperLock.class);
	private static final ConcurrentHashMap<String, ReentrantLock> JVM_LOCAL_LOCKS = new ConcurrentHashMap<String, ReentrantLock>();
	private ReentrantLock jvmLock;
	private WriteLock lock;
	private String path;
	private static final int REPEAT_COUNT = 3;
	private static final int SLEEP_TIME = 500;

	public LocalZookeeperLock(ZooKeeper zk, String root, String lockPath) {
		path = formatPath(root, lockPath);
		// 防止出现冲突，这儿使用锁路径作为key
		jvmLock = JVM_LOCAL_LOCKS.get(path);
		if (jvmLock == null) {
			jvmLock = new ReentrantLock();
			JVM_LOCAL_LOCKS.put(path, jvmLock);
		}
		lock = new WriteLock(zk, path, null);
	}

	public void lock() throws InterruptedException {
		jvmLock.lock();
		try {
			// 不能设置成只有获取到锁了，才让它停止，这样会生成很大的流量和耗费很多资源
			// 原有实现机制中，包含了重试获取锁的功能，所以这儿只进行一次或者有限次数，如果还是获取不到可以报FATAL，提示可能存在死锁问题
			for (int i = 1; i <= REPEAT_COUNT; i++) {
				if (lock.lock()) {
					return;
				}
				Thread.sleep(SLEEP_TIME * i);
			}
			LOG.fatal("获取锁" + path + "失败，请检查是否存在死锁问题.");
			throw new InterruptedException();
		} catch (Exception e) {
			// here need catch Throwable, avoid throw
			// runtimeException
			// cause the jvmLock unrealsed
			LOG.error("accquire global lock " + path + " fail.", e);
			jvmLock.unlock();
			throw new InterruptedException();
		}
	}

	public void unlock() {
		try {
			lock.unlock();
		} finally {
			// here ensure release the jvm lock
			jvmLock.unlock();
		}
	}

	public static boolean isContainJvmLock(String name) {
		return JVM_LOCAL_LOCKS.containsKey(name);
	}
}
