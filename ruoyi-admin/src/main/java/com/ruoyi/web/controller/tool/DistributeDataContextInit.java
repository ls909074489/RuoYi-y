package com.ruoyi.web.controller.tool;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.framework.distributed.ClusterExecute;
import com.ruoyi.framework.distributed.impl.UserAccountMessageClusterExecute;
import com.ruoyi.framework.distributed.utils.DistributedRedisUtil;
import com.ruoyi.framework.distributed.vo.ClusterMessage;
import com.ruoyi.framework.distributed.vo.DistributedData;
import com.ruoyi.framework.util.applicationcontext.ApplicationContextInit;
import com.ruoyi.framework.util.applicationcontext.SpringContextUtil;
import com.ruoyi.system.domain.SysUser;


/**
 * 实现了ApplicationContextInit会在SpringContextUtil项目初始完成执行init方法
 * @author liusheng
 *
 */
@Component
public class DistributeDataContextInit implements ApplicationContextInit {

	private static Log logger = LogFactory.getLog(DistributeDataContextInit.class);
	
	@Autowired
	private DistributedRedisUtil distributedUtil;

	@Override
	public void init() {
		logger.info("初始化分布式bean 》》》》》》》》");
		List<ClusterExecute> list = SpringContextUtil.getBeansOfType(ClusterExecute.class);
		for(ClusterExecute ce:list){
			String classFullPath = ce.getClass().getCanonicalName();
			DistributedData.getInstance().add(classFullPath, (ClusterExecute) ce);
			logger.info("注入集群方法:" + classFullPath);
			logger.info("****************************Spring容器启动完成执行初始化方法完成:" + ce.getClass().getName());
		}
		SysUser msgData=new SysUser();
    	msgData.setUserName("测试项目初始化上下文执行方法TestContextInit》》》》》》");
		//分布式推送
		distributedUtil.executeInCluster(new ClusterMessage(UserAccountMessageClusterExecute.class.getCanonicalName(), msgData));
	}

	@Override
	public int sort() {
		return 0;
	}

}