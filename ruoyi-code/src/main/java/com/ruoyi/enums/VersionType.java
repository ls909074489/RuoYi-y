package com.ruoyi.enums;
/**
 * 版本服务器管理类型
 * 
 * @author caozhejun
 *
 */
public enum VersionType {

	SVN("svn", "SVN"),

	GIT("git", "GIT");

	private String name;

	private String description;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	private VersionType(String name, String description) {
		this.name = name;
		this.description = description;
	}

}