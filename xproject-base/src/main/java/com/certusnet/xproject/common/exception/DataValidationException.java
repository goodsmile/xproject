package com.certusnet.xproject.common.exception;

/**
 * 用于数据校验的Exception
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class DataValidationException extends SystemException {

	private static final long serialVersionUID = 1L;

	public DataValidationException(String message) {
		super(message);
	}

	public DataValidationException(Throwable cause) {
		super(cause);
	}

	public DataValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataValidationException(String code, String message) {
		super(code, message);
	}
	
	public DataValidationException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
}
