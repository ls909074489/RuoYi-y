package com.ruoyi.framework.distributed.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.ruoyi.framework.distributed.ClusterExecute;
import com.ruoyi.system.domain.SysUser;

@Component
public class UserAccountMessageClusterExecute implements ClusterExecute {

	private static final Log logger = LogFactory.getLog(UserAccountMessageClusterExecute.class);


	@Override
	public void execute(Object data) {
		if (!(data instanceof SysUser)) {
			throw new RuntimeException("类型错误：" + data.getClass().getCanonicalName());
		}
		SysUser messageData = (SysUser) data;
		logger.info("接收到集群消息做相应的操作》》》》》》》》》》" + messageData.getUserName());
	}

}