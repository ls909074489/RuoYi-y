package com.ruoyi.common.exception;

/**
 * 业务异常类
 * 
 * @author caozj
 * 
 */
public class BussException extends RuntimeException {

	private static final long serialVersionUID = -2320399934788651830L;

	public BussException(String message) {
		super(message);
	}
}
