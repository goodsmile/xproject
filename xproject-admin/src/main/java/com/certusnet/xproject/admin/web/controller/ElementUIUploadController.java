package com.certusnet.xproject.admin.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.certusnet.xproject.common.consts.GlobalConstants;
import com.certusnet.xproject.common.support.ImagePixel;
import com.certusnet.xproject.common.support.Result;
import com.certusnet.xproject.common.util.FileUtils;
import com.certusnet.xproject.common.util.ImageUtils;
import com.certusnet.xproject.common.util.StringUtils;
import com.certusnet.xproject.common.util.UUIDUtils;
import com.certusnet.xproject.common.web.BaseController;

@RestController
public class ElementUIUploadController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ElementUIUploadController.class);
	
	/**
	 * 默认的上传图片长宽允许的误差
	 */
	public static final Integer DEFAULT_IMAGE_PIXEL_DEVIATION = 10;
	
	@Autowired
	private CommonsMultipartResolver multipartResolver;
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @param formatLimit		- 上传图片限制的格式,例如"jpg,png"
	 * @param pixelLimit		- 上传图片像素限制,例如 "200x200", "600x*"
	 * @param pixelDeviation	- 上传图片像素宽高浮动限制, 例如宽高上下浮动10个像素也是允许的
	 * @return
	 */
	@RequestMapping(value="/upload/image/submit", method=POST)
	public Object uploadImage(HttpServletRequest request, HttpServletResponse response, String formatLimit, String pixelLimit, Integer pixelDeviation) {
		List<String> fileFormats = GlobalConstants.DEFAULT_UPLOAD_IMAGE_FORMATS;
		pixelDeviation = pixelDeviation == null ? DEFAULT_IMAGE_PIXEL_DEVIATION : pixelDeviation;
		List<Object> dataList = new ArrayList<Object>();
		Result<Object> result = new Result<Object>();
		result.setCode("200");
		result.setMessage("上传成功!");
		try {
			ImagePixel imagePixel = null;
			if(!StringUtils.isEmpty(pixelLimit)){
				imagePixel = ImagePixel.createImagePixel(pixelLimit);
			}
			if(!StringUtils.isEmpty(formatLimit)){
				fileFormats = Arrays.asList(formatLimit.split(","));
			}
			String contextPath = request.getContextPath();
			String httpContextPath = request.getRequestURL().toString();
			httpContextPath = httpContextPath.substring(0, httpContextPath.indexOf(contextPath) + contextPath.length() + 1);
			
			String realContextPath = request.getSession().getServletContext().getRealPath("/");
			realContextPath = FileUtils.formatFilePath(realContextPath);
			//判断 request 是否有文件上传,即多部分请求  
	        if(multipartResolver.isMultipart(request)){
	            //转换成多部分request
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	            //取得request中的所有文件名
	            Iterator<String> fileNames = multiRequest.getFileNames();
	            while(fileNames.hasNext()){
	                //记录上传过程起始时的时间，用来计算上传时间
	                int start = (int) System.currentTimeMillis();
	                //取得上传文件  
	                MultipartFile file = multiRequest.getFile(fileNames.next());
	                if(file != null){
	                    //取得当前上传文件的文件名称
	                    String originalFileName = file.getOriginalFilename();
	                    //如果名称不为空,说明该文件存在，否则说明该文件不存在
	                    if(!StringUtils.isEmpty(originalFileName)){
	                    	originalFileName = originalFileName.trim().toLowerCase();
	                        logger.info(">>> Upload original file name : " + originalFileName);
	                        String contentType = file.getContentType().toLowerCase();
	                        String fileFormat = StringUtils.stripStart(contentType, "image/");
	                        if(!fileFormats.contains(fileFormat)){
	                        	result.setCode("500");
	            	        	result.setMessage(String.format("上传文件(%s)必须是格式为：%s的图片文件!", originalFileName, fileFormats));
	            	        	break;
	                        }
	                        if(imagePixel != null){
	                        	ImagePixel targetImagePixel = ImageUtils.getImagePixel(file.getInputStream());
	                        	if(imagePixel.getWidth() != null && Math.abs(imagePixel.getWidth() - targetImagePixel.getWidth()) > pixelDeviation){
	                        		result.setCode("500");
		            	        	result.setMessage("上传图片像素宽度超出限制!");
		            	        	break;
	                        	}
	                        	if(imagePixel.getHeight() != null && Math.abs(imagePixel.getHeight() - targetImagePixel.getHeight()) > pixelDeviation){
	                        		result.setCode("500");
		            	        	result.setMessage("上传图片像素高度超出限制!");
		            	        	break;
	                        	}
	                        }
	                        //重命名上传后的文件名
	                        String renamedFileName = UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
	                        logger.info(">>> Upload renamed file name : " + renamedFileName);
	                        String fileRelativePath = GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH + renamedFileName;
	                        logger.info(">>> Upload file save path : " + fileRelativePath);
	                        //定义上传路径
	                        File destFile = new File(realContextPath + fileRelativePath);
	                        file.transferTo(destFile);
	                        Map<String,Object> data = new HashMap<String,Object>();
	                        data.put("name", originalFileName);
	                        data.put("url", FileUtils.formatFilePath(httpContextPath + fileRelativePath));
	                        data.put("path", fileRelativePath);
	                        dataList.add(data);
	                    }
	                }
	                //记录上传该文件后的时间
	                int end = (int) System.currentTimeMillis();
	                logger.info(">>> Upload file cost time : {} ms", (end - start));
	            }
	        }else{
	        	result.setCode("500");
	        	result.setMessage("请求中未发现有文件上传!");
	        }
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			result.setCode("500");
        	result.setMessage("上传文件出错：" + e.getMessage());
		}
		if(!dataList.isEmpty()){
			result.setData(dataList.size() == 1 ? dataList.get(0) : dataList);
		}
		response.setStatus(Integer.valueOf(result.getCode()));
		return result;
	}
	
	/**
	 * 上传普通文件
	 * @param request
	 * @param response
	 * @param formatLimit		- 上传文件限制的格式,例如"jpg,png"
	 * @return
	 */
	@RequestMapping(value="/upload/file/submit", method=POST)
	public Object uploadFile(HttpServletRequest request, HttpServletResponse response, String formatLimit) {
		List<String> fileFormats = null;
		List<Object> dataList = new ArrayList<Object>();
		Result<Object> result = new Result<Object>();
		result.setCode("200");
		result.setMessage("上传成功!");
		try {
			if(!StringUtils.isEmpty(formatLimit)){
				fileFormats = Arrays.asList(formatLimit.split(","));
			}
			String contextPath = request.getContextPath();
			String httpContextPath = request.getRequestURL().toString();
			httpContextPath = httpContextPath.substring(0, httpContextPath.indexOf(contextPath) + contextPath.length() + 1);
			
			String realContextPath = request.getSession().getServletContext().getRealPath("/");
			realContextPath = FileUtils.formatFilePath(realContextPath);
			//判断 request 是否有文件上传,即多部分请求  
	        if(multipartResolver.isMultipart(request)){
	            //转换成多部分request
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	            //取得request中的所有文件名
	            Iterator<String> fileNames = multiRequest.getFileNames();
	            while(fileNames.hasNext()){
	                //记录上传过程起始时的时间，用来计算上传时间
	                int start = (int) System.currentTimeMillis();
	                //取得上传文件  
	                MultipartFile file = multiRequest.getFile(fileNames.next());
	                if(file != null){
	                    //取得当前上传文件的文件名称
	                    String originalFileName = file.getOriginalFilename();
	                    //如果名称不为空,说明该文件存在，否则说明该文件不存在
	                    if(!StringUtils.isEmpty(originalFileName)){
	                    	originalFileName = originalFileName.trim().toLowerCase();
	                        logger.info(">>> Upload original file name : " + originalFileName);
	                        String fileFormat = FileUtils.getFileFormat(originalFileName);
	                        if(fileFormats != null && !fileFormats.contains(fileFormat)){
	                        	result.setCode("500");
	            	        	result.setMessage(String.format("上传文件(%s)必须是：%s格式的文件!", originalFileName, fileFormats));
	            	        	break;
	                        }
	                        //重命名上传后的文件名
	                        String renamedFileName = UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
	                        logger.info(">>> Upload renamed file name : " + renamedFileName);
	                        String fileRelativePath = GlobalConstants.DEFAULT_UPLOAD_SAVE_PATH + renamedFileName;
	                        logger.info(">>> Upload file save path : " + fileRelativePath);
	                        //定义上传路径
	                        File destFile = new File(realContextPath + fileRelativePath);
	                        file.transferTo(destFile);
	                        Map<String,Object> data = new HashMap<String,Object>();
	                        data.put("name", originalFileName);
	                        data.put("url", FileUtils.formatFilePath(httpContextPath + fileRelativePath));
	                        data.put("path", fileRelativePath);
	                        dataList.add(data);
	                    }
	                }
	                //记录上传该文件后的时间
	                int end = (int) System.currentTimeMillis();
	                logger.info(">>> Upload file cost time : {} ms", (end - start));
	            }
	        }else{
	        	result.setCode("500");
	        	result.setMessage("请求中未发现有文件上传!");
	        }
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			result.setCode("500");
        	result.setMessage("上传文件出错：" + e.getMessage());
		}
		if(!dataList.isEmpty()){
			result.setData(dataList.size() == 1 ? dataList.get(0) : dataList);
		}
		response.setStatus(Integer.valueOf(result.getCode()));
		return result;
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param path
	 * @return
	 */
	@RequestMapping(value="/upload/remove/submit")
	public Object removeFile(HttpServletRequest request, HttpServletResponse response, String path) {
		String message = "删除成功!";
		try {
			if(!StringUtils.isEmpty(path)){
				String realContextPath = request.getSession().getServletContext().getRealPath("/");
				String fileRealPath = FileUtils.formatFilePath(realContextPath + path);
				FileUtils.deleteFileQuietly(fileRealPath);
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			message = "删除失败!";
		}
		return genSuccessResult(message, null);
	}
	
}
