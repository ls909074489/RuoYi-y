package com.ruoyi.framework.distributed.utils;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ruoyi.framework.distributed.DistributedInterface;
import com.ruoyi.framework.distributed.impl.RedisReceiveMessage;
import com.ruoyi.framework.distributed.vo.ClusterMessage;
import com.ruoyi.framework.util.SerializeUtil;

/**
 * redis分布式消息
 * 	@Autowired
	private DistributedRedisUtil distributedUtil;
	
	distributedUtil.executeInCluster(new ClusterMessage(UserAccountMessageClusterExecute.class.getCanonicalName(), msgData));
 * @author liusheng
 *
 */
@Component
public class DistributedRedisUtil {

	private static final Log logger = LogFactory.getLog(DistributedRedisUtil.class);

	@Autowired
	private DistributedInterface di;

	@Value("${distributed.channel}")
	private String channel;

	private byte[] key;

	@PostConstruct
	private void init() {
		key = SerializeUtil.serialize(channel);
		// 订阅频道
		di.subscribe(key, new RedisReceiveMessage());
		logger.info("redis订阅集群频道成功:"+channel);
	}

	/**
	 * 发送一个消息对象给集群中的所有服务器,通知所有服务器执行方法
	 * 
	 * @param message
	 *            消息对象
	 */
	public void executeInCluster(ClusterMessage message) {
		di.publish(key, SerializeUtil.serialize(message));
	}

}