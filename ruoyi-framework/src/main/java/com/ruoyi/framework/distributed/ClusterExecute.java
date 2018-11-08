package com.ruoyi.framework.distributed;

/**
 * 集群可以通知执行方法的接口
 * 
 * @author caozj
 *
 */
public interface ClusterExecute {

  /**
   * 执行
   * 
   * @param data 参数对象
   */
  void execute(Object data);

}
