package com.ruoyi.framework.distributed;

/**
 * 接收消息的接口
 * @author liusheng
 *
 */
public interface ReceiveMessageInterface {

	/**
	 * 接收消息
	 * 
	 * @param channel
	 *            频道
	 * @param message
	 *            消息内容
	 */
	public void onMessage(byte[] channel, byte[] message);
}
