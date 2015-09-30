package com.wms.studio.cache.redis;

import com.wms.studio.cache.api.Cache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Redis缓存接口的实现。
 *
 * @author hzwumsh
 *
 * @param <K>
 *            缓存key
 * @param <V>
 *            缓存value
 */
public class RedisCache<K, V> implements Cache<K, V> {

	/**
	 * 私有的内部日志实例
	 */
	private static final Logger log = Logger.getLogger(RedisCache.class);

	@Autowired
	private RedisTemplate<K, V> template;

	private final String name;

	/**
	 * 通过一个JedisManager实例构造RedisCache Constructs a cache instance with the
	 * specified Redis manager and using a custom key prefix.
	 *
	 * @param name
	 *            The Redis key prefix
	 */
	public RedisCache(String name) {

		Validate.notBlank(name, "缓存名称不允许为空");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(final K key) {
		log.debug("根据key从Redis中获取对象 key [" + key + "]");

		if(key instanceof String)
		{
			return template.opsForValue().get(name+"."+key);
		}

		return template.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(final K key, final V value) {
		return (V) execute(new RedisCallback(RedisTemplate<K, V> template) {
			public Object doWithRedis(Jedis jedis) {
				jedis.set(getByteKey(key), SerializeUtils.serialize(value));
				return value;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(final K key, final V value, final int expire)
			 {

		return (V) execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				jedis.set(getByteKey(key), SerializeUtils.serialize(value));
				jedis.expire(getByteKey(key), expire);
				return value;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(final K key)  {
		log.debug("从redis中删除 key [" + key + "]");
		return (V) execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				jedis.expire(getByteKey(key), 0);
				return null;
			}
		});
	}

	@Override
	public void clear()  {
		log.debug("从redis中删除所有元素");
		execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				Set<byte[]> keys = jedis.keys(getByteKey("*"));
				for (byte[] key : keys) {
					jedis.del(key);
				}
				return null;
			}
		});
	}

	@Override
	public int size() {
		return (Integer) execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				return jedis.dbSize().intValue();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {

		return (Collection<V>) execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {

				Set<byte[]> keys = jedis.keys(getByteKey("*"));
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.emptySet();
				}
				List<V> values = new ArrayList<V>(keys.size());
				for (byte[] key : keys) {
					V value = (V) SerializeUtils.deserialize(jedis.get(key));
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			}
		});

	}

	@Override
	public void flushDB() {

		execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {

				jedis.flushDB();
				return null;
			}
		});

	}

}
