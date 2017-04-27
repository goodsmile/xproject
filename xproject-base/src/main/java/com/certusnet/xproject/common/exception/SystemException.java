package com.certusnet.xproject.common.exception;

import com.certusnet.xproject.common.consts.GlobalConstants;

/**
 * 系统异常基类
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月3日 上午11:26:00
 * @version  	1.0
 */
public abstract class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code = GlobalConstants.RESULT_CODE_FAILURE;
	
	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(String code, String message) {
		super(message);
		setCode(code);
	}
	
	public SystemException(String code, String message, Throwable cause) {
		super(message, cause);
		setCode(code);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
