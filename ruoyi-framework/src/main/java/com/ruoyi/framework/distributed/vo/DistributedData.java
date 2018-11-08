package com.ruoyi.framework.distributed.vo;

import java.util.HashMap;
import java.util.Map;

import com.ruoyi.framework.distributed.ClusterExecute;

/**
 * 缓存集群通知要执行的方法数据
 * 
 * @author caozj
 *
 */
public class DistributedData {

  Map<String, ClusterExecute> dataMap = null;

  private DistributedData() {
    dataMap = new HashMap<>();
  }

  private static final class DistributedDataHolder {
    private static final DistributedData instance = new DistributedData();
  }

  public static DistributedData getInstance() {
    return DistributedDataHolder.instance;
  }

  /**
   * 增加一条集群可以通知执行的方法记录
   * 
   * @param classFullPath 类的完整路径
   * @param execute 要执行的类对象
   */
  public void add(String classFullPath, ClusterExecute execute) {
    dataMap.put(classFullPath, execute);
  }

  /**
   * 根据类的完整路径，获取可以执行的对象
   * 
   * @param classFullPath
   * @return
   */
  public ClusterExecute get(String classFullPath) {
    return dataMap.get(classFullPath);
  }


}
