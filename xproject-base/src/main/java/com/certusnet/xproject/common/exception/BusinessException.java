package com.certusnet.xproject.common.exception;

/**
 * 用于业务的Exception,可以设置业务异常代码
 * 
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class BusinessException extends SystemException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String code, String message) {
		super(code, message);
	}
	
	public BusinessException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
