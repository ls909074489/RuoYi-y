package com.ruoyi.framework.distributed.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ruoyi.framework.distributed.ClusterExecute;
import com.ruoyi.framework.distributed.ReceiveMessageInterface;
import com.ruoyi.framework.distributed.vo.ClusterMessage;
import com.ruoyi.framework.distributed.vo.DistributedData;
import com.ruoyi.framework.util.SerializeUtil;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 * 订阅Redis的消息
 * 
 * @author caozj
 *
 */
public class RedisReceiveMessage extends BinaryJedisPubSub implements ReceiveMessageInterface {

  private static final Log logger = LogFactory.getLog(RedisReceiveMessage.class);

  @Override
  public void onMessage(byte[] channel, byte[] message) {
    ClusterMessage clusterMessage = (ClusterMessage) SerializeUtil.unserialize(message);
    ClusterExecute execute = DistributedData.getInstance().get(clusterMessage.getClassFullPath());
    if (execute != null) {
      execute.execute(clusterMessage.getData());
      logger.info(clusterMessage.getClassFullPath() + "执行完成");
    } else {
      logger.warn("没有找到" + clusterMessage.getClassFullPath());
    }
  }

}