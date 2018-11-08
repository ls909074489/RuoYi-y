package com.ruoyi.framework.distributed.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.distributed.DistributedInterface;
import com.ruoyi.framework.distributed.ReceiveMessageInterface;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 分布式发布订阅实现类
 * 
 * @author caozj
 *
 */
@Component
public class RedisDistributedImpl implements DistributedInterface {

	private static final Log logger = LogFactory.getLog(RedisDistributedImpl.class);

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort = 6379;

	@Value("${spring.redis.password}")
	private String redisPassword;

	private int timeout = Constants.REDIS_TOMEOUT;

	private JedisPool pool4Subscribe = null;

	private JedisPool pool4Publish = null;

	@PostConstruct
	public void init() {
		if (StringUtils.isEmpty(redisHost)) {
			logger.info("============spring.redis.host没配置,不能使用分布式功能");
			return;
		}
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(200);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(1000 * 30);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			if (StringUtils.isEmpty(redisPassword)) {
				pool4Subscribe = new JedisPool(config, redisHost, redisPort, timeout);
				pool4Publish = new JedisPool(config, redisHost, redisPort, timeout);
			} else {
				pool4Subscribe = new JedisPool(config, redisHost, redisPort, timeout, redisPassword);
				pool4Publish = new JedisPool(config, redisHost, redisPort, timeout, redisPassword);
			}
		} catch (Exception e) {
			throw new RuntimeException("不能初始化Redis客户端，不能使用分布式功能", e);
		}
	}

	@Override
	public void publish(byte[] channel, byte[] message) {
		Jedis jedis = null;
		try {
			jedis = pool4Publish.getResource();
			long num = jedis.publish(channel, message);
			logger.info("发布消息成功，共有订阅方" + num + "个");
		} catch (Exception e) {
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void subscribe(byte[] channel, ReceiveMessageInterface receiveMessage) {
		if (!(receiveMessage instanceof BinaryJedisPubSub)) {
			throw new RuntimeException(receiveMessage.getClass().getCanonicalName() + "类型错误");
		}
		final Jedis jedis = pool4Subscribe.getResource();
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					BinaryJedisPubSub edisPubSub = (BinaryJedisPubSub) receiveMessage;
					jedis.subscribe(edisPubSub, channel);
				}
			}).start();
		} catch (Exception e) {
			throw new RuntimeException("Redis出现错误！", e);
		}
	}

	private void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
