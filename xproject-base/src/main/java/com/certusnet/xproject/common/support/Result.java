package com.certusnet.xproject.common.support;

import com.certusnet.xproject.common.consts.GlobalConstants;

/**
 * 通用返回结果类
 *
 * @param <T>
 * @author pengpeng
 * @version 1.0
 * @date 2014年6月13日 上午8:59:37
 */
public class Result<T> {

	/** 成功与否 */
    private boolean success;

    /** 结果代码 */
    private String code;
    
    /** 消息 */
    private String message;
    
    /** 结果数据 */
    private T data;
    
	public Result() {
		super();
	}
	
	public Result(boolean success) {
		this(success, null, null, null);
	}
	
	public Result(boolean success, String message) {
		this(success, null, message, null);
	}
	
	public Result(boolean success, T data) {
		this(success, null, null, data);
	}
	
	public Result(boolean success, String code, String message) {
		this(success, code, message, null);
	}

	public Result(boolean success, String code, String message, T data) {
		super();
		this.success = success;
		if(code == null){
			this.code = success ? GlobalConstants.RESULT_CODE_SUCCESS : GlobalConstants.RESULT_CODE_FAILURE;
		}else{
			this.code = code;
		}
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String toString() {
		return "Result [success=" + success + ", code=" + code + ", message="
				+ message + ", data=" + data + "]";
	}
	
}
