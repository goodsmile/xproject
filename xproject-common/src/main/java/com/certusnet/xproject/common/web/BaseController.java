package com.certusnet.xproject.common.web;

import javax.servlet.http.HttpServletRequest;

import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.Result;

public abstract class BaseController {

	@SuppressWarnings("unchecked")
	protected <T> T getRequestAttribute(HttpServletRequest request, String key) {
		return (T) request.getAttribute(key);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getSessionAttribute(HttpServletRequest request, String key) {
		return (T) request.getSession().getAttribute(key);
	}
	
	protected void setRequestAttribute(HttpServletRequest request, String key, Object value) {
		request.setAttribute(key, value);
	}
	
	protected void setSessionAttribute(HttpServletRequest request, String key, Object value) {
		request.getSession().setAttribute(key, value);
	}
	
	protected void removeRequestAttribute(HttpServletRequest request, String key) {
		request.removeAttribute(key);
	}
	
	protected void removeSessionAttribute(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}
	
	protected Result<Object> genSuccessResult(Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(true);
		result.setCode(GlobalConstants.RESULT_CODE_SUCCESS);
		result.setData(data);
		return result;
	}
	
	protected Result<Object> genSuccessResult(String message, Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(true);
		result.setCode(GlobalConstants.RESULT_CODE_SUCCESS);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
	
	protected Result<Object> genSuccessResult(String code, String message, Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(true);
		result.setCode(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
	
	protected Result<Object> genFailureResult(Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(false);
		result.setCode(GlobalConstants.RESULT_CODE_FAILURE);
		result.setData(data);
		return result;
	}
	
	protected Result<Object> genFailureResult(String message, Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(false);
		result.setCode(GlobalConstants.RESULT_CODE_FAILURE);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
	
	protected Result<Object> genFailureResult(String code, String message, Object data) {
		Result<Object> result = new Result<Object>();
		result.setSuccess(false);
		result.setCode(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
	
}
