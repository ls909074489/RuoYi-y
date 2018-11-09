package com.ruoyi.model;

/**
 * 接收发布包的form对象
 * 
 * @author caozhejun
 *
 */
public class DeployForm {

	private String sign;

	private String startParams;

	private String time;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStartParams() {
		return startParams;
	}

	public void setStartParams(String startParams) {
		this.startParams = startParams;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeployForm [sign=");
		builder.append(sign);
		builder.append(", startParams=");
		builder.append(startParams);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}

}
