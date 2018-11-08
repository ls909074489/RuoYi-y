package com.ruoyi.framework.distributed.vo;

import java.io.Serializable;

/**
 * 分布式消息对象
 * 
 * @author caozj
 *
 */
public class ClusterMessage implements Serializable {

	private static final long serialVersionUID = 7816563326351524732L;

	/**
	 * 要执行的类的完整路径
	 */
	private String classFullPath;

	/**
	 * 数据参数对象
	 */
	private Object data;

	public ClusterMessage() {
	}

	public ClusterMessage(String classFullPath, Object data) {
		this.classFullPath = classFullPath;
		this.data = data;
	}

	public String getClassFullPath() {
		return classFullPath;
	}

	public void setClassFullPath(String classFullPath) {
		this.classFullPath = classFullPath;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClusterMessage [classFullPath=");
		builder.append(classFullPath);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
