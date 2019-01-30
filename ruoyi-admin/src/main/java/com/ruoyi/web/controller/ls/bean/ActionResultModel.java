package com.ruoyi.web.controller.ls.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回bean
 * @author liusheng
 *
 */
public class ActionResultModel<T> {

	private boolean suc=false;
	private String msg="";
	private List<T> records=new ArrayList<T>();
	
	public boolean isSuc() {
		return suc;
	}
	public void setSuc(boolean suc) {
		this.suc = suc;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}

	

}
