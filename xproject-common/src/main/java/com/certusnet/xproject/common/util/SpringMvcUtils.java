package com.certusnet.xproject.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
/**
 * SpringMVC的工具类
 * 
 * @author 	pengpeng
 * @date   		2017年5月13日 上午10:08:47
 * @version 	1.0
 */
public class SpringMvcUtils {

	/**
	 * 判断请求是否是异步请求
	 * @param request
	 * @param handler
	 * @return
	 */
	public static boolean isAsyncRequest(HttpServletRequest request, Object handler){
		boolean isAsync = false;
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			isAsync = handlerMethod.hasMethodAnnotation(ResponseBody.class);
			if(!isAsync){
				Class<?> controllerClass = handlerMethod.getBeanType();
				isAsync = controllerClass.isAnnotationPresent(ResponseBody.class) || controllerClass.isAnnotationPresent(RestController.class);
			}
			if(!isAsync){
				isAsync = ResponseEntity.class.equals(handlerMethod.getMethod().getReturnType());
			}
		}
		if(!isAsync){
			isAsync = HttpUtils.isAsynRequest(request);
		}
		return isAsync;
	}
	
}
