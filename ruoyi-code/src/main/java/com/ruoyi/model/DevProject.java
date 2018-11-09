package com.ruoyi.model;

import java.util.Date;

/**
 * 项目
 */
public class DevProject {

	/** */
	private int id;

	/**
	 * 编号
	 * 
	 */
	private String code;

	/**
	 * 名称
	 * 
	 */
	private String name;

	/**
	 * 版本管理类型(svn/git)
	 * 
	 */
	private String versionType;

	/**
	 * 账号
	 * 
	 */
	private String account;

	/**
	 * 密码
	 * 
	 */
	private String pwd;

	/**
	 * 版本管理地址
	 * 
	 */
	private String versionUrl;

	/**
	 * 打包命令文件路径
	 * 
	 */
	private String packageCommandFile;

	/**
	 * 服务启动参数
	 * 
	 */
	private String startParams;

	/**
	 * 通知邮件地址列表
	 */
	private String notifyEmails;

	/**
	 * 时间
	 * 
	 */
	private Date createTime;

	//////////// 用于页面显示，不保存到数据库////////////
	/**
	 * 打包结果文件路径
	 */
	private String targetFiles;

	/**
	 * 部署的服务器
	 */
	private String targetServers;

	public String getTargetServers() {
		return targetServers;
	}

	public void setTargetServers(String targetServers) {
		this.targetServers = targetServers;
	}

	public String getTargetFiles() {
		return targetFiles;
	}

	public void setTargetFiles(String targetFiles) {
		this.targetFiles = targetFiles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}

	public String getPackageCommandFile() {
		return packageCommandFile;
	}

	public void setPackageCommandFile(String packageCommandFile) {
		this.packageCommandFile = packageCommandFile;
	}

	public String getStartParams() {
		return startParams;
	}

	public void setStartParams(String startParams) {
		this.startParams = startParams;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNotifyEmails() {
		return notifyEmails;
	}

	public void setNotifyEmails(String notifyEmails) {
		this.notifyEmails = notifyEmails;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DevProject [id=");
		builder.append(id);
		builder.append(", code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", versionType=");
		builder.append(versionType);
		builder.append(", account=");
		builder.append(account);
		builder.append(", pwd=");
		builder.append(pwd);
		builder.append(", versionUrl=");
		builder.append(versionUrl);
		builder.append(", packageCommandFile=");
		builder.append(packageCommandFile);
		builder.append(", startParams=");
		builder.append(startParams);
		builder.append(", notifyEmails=");
		builder.append(notifyEmails);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", targetFiles=");
		builder.append(targetFiles);
		builder.append(", targetServers=");
		builder.append(targetServers);
		builder.append("]");
		return builder.toString();
	}

}