package com.certusnet.xproject.common.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.certusnet.xproject.common.util.StringUtils;

/**
 * Http访问日志记录之Servlet输入输出流过滤器,解决：
 * 1、HttpServletRequest的输入流可重读,
 * 2、HttpServletResponse的输出流可缓存
 * 
 * @author 	pengpeng
 * @date   		2017年5月16日 下午12:47:22
 * @version 	1.0
 */
public class HttpAccessLoggingServletStreamFilter extends OncePerRequestFilter {

	private static final MediaType DEFAULT_SUPPORTED_REQ_CONTENTTYPE = MediaType.APPLICATION_JSON;
	
	/**
	 * 对于支持的请求体类型进行请求Wrapper
	 */
	private final Set<MediaType> supportedReqContentTypes = new HashSet<MediaType>();
	
	public HttpAccessLoggingServletStreamFilter() {
		super();
		supportedReqContentTypes.add(DEFAULT_SUPPORTED_REQ_CONTENTTYPE);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest requestToUse = request;
		HttpServletResponse responseToUse = response;
		if(isRequestBodySupport(request)){
			requestToUse = new ContentCachingRequestWrapper(request);
			responseToUse = new ContentCachingResponseWrapper(response);
		}
		filterChain.doFilter(requestToUse, responseToUse);
		ContentCachingResponseWrapper contentCachedResponse = getContentCachingResponseWrapper(responseToUse);
		if(contentCachedResponse != null){
			contentCachedResponse.copyBodyToResponse(); //重写响应到response.OutputStream中去,否则客户端响应会出现NO CONTENT
		}
	}

	public Set<MediaType> getSupportedReqContentTypes() {
		return supportedReqContentTypes;
	}

	public void setSupportedReqContentTypes(String supportedReqContentTypes) {
		addSupportedContentTypes(this.supportedReqContentTypes, supportedReqContentTypes);
	}
	
	protected void addSupportedContentTypes(Set<MediaType> supportedContentTypes,String supportedContentTypeValues) {
		if(!StringUtils.isEmpty(supportedContentTypeValues)){
			String[] contentTypes = supportedContentTypeValues.split(",");
			for(String contentType : contentTypes){
				if(!StringUtils.isEmpty(contentType)){
					supportedContentTypes.add(MediaType.valueOf(contentType.trim()));
				}
			}
		}
	}
	
	protected boolean isRequestBodySupport(HttpServletRequest request) {
		boolean asyncRequest = !isAsyncDispatch(request) && !isAsyncStarted(request);
		String contentTypeValue = request.getContentType();
		MediaType contentType = getContentType(contentTypeValue);
		boolean supported = false;
		if(asyncRequest && contentType != null){
			if(supportedReqContentTypes.isEmpty()){
				supported = false;
			}else{
				for(MediaType mediaType : supportedReqContentTypes){
					if(mediaType.getType().equals(contentType.getType())){ //主类型相同即可
						supported = true;
						break;
					}
				}
			}
		}
		return supported;
	}
	
	/**
	 * 获取ContentCachingResponseWrapper
	 * (考虑到Wrapper是可以重复的,故需要递归获取)
	 * @param response
	 * @return
	 */
	protected ContentCachingResponseWrapper getContentCachingResponseWrapper(ServletResponse response) {
		if(response instanceof ContentCachingResponseWrapper){
			return (ContentCachingResponseWrapper) response;
		}else if(response instanceof HttpServletResponseWrapper) {
			HttpServletResponseWrapper responseToUse = (HttpServletResponseWrapper) response;
			return getContentCachingResponseWrapper(responseToUse.getResponse());
		}
		return null;
	}
	
	protected MediaType getContentType(String contentType) {
		try {
			if(!StringUtils.isEmpty(contentType)){
				return MediaType.valueOf(contentType);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
}
