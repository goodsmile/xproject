package com.certusnet.xproject.common.web.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.springframework.web.util.WebUtils;

/**
 * 可重读的HttpServletRequest请求体
 * 
 * @author 	pengpeng
 * @date   		2017年5月18日 下午12:39:59
 * @version 	1.0
 */
public class RereadableStreamHttpServletRequest extends HttpServletRequestWrapper {

	private final byte[] rawData;
	
	public byte[] getRawData() {
		return rawData;
	}

	public RereadableStreamHttpServletRequest(HttpServletRequest request) {
		super(request);
		try {
			rawData = IOUtils.toByteArray(request.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
    public ServletInputStream getInputStream() throws IOException {
        return new RereadableServletInputStream(new ByteArrayInputStream(getRawData()));
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }
    
    public String getCharacterEncoding() {
		String enc = super.getCharacterEncoding();
		return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
	}
    
    private class RereadableServletInputStream extends ServletInputStream {

    	private boolean finished = false;
    	
    	private InputStream stream;
    	
		public RereadableServletInputStream(InputStream stream) {
			super();
			this.stream = stream;
		}

		public boolean isFinished() {
			return finished;
		}

		public boolean isReady() {
			return true;
		}

		public void setReadListener(ReadListener readListener) {
			
		}

		public int read() throws IOException {
			int b = stream.read();
			if(b == -1){
				finished = true;
			}
			return b;
		}
		
	}
	
}
