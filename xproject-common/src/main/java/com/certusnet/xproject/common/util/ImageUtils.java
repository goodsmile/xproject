package com.certusnet.xproject.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteRender;
import com.alibaba.simpleimage.util.ImageReadHelper;

/**
 * 基于simpleimage的图片处理类
 * 
 * @author 	pengpeng
 * @date   		2017年2月17日 上午10:26:55
 * @version 	1.0
 */
public class ImageUtils {

	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static Dimension getDimension(String fullImgPath) {
		return getDimension(new File(fullImgPath));
	}
	
	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static Dimension getDimension(File imgFile) {
		InputStream in = null;
		try {
			in = new FileInputStream(imgFile);
			ImageWrapper imageWrapper = ImageReadHelper.read(in);
			return new Dimension(imageWrapper.getWidth(), imageWrapper.getHeight());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * 只支持等比缩小(不可放大)
	 * @param srcImgFilePath
	 * @param destImgFilePath
	 * @param scaleParameter
	 */
	public static void scaleImage(String srcImgFilePath, String destImgFilePath, ScaleParameter scaleParameter) {
		scaleImage(new File(srcImgFilePath), new File(destImgFilePath), scaleParameter);
	}
	
	/**
	 * 只支持等比缩小(不可放大)
	 * @param srcImgFilePath
	 * @param destImgFilePath
	 * @param scaleParameter
	 */
	public static void scaleImage(File srcImgFile, File destImgFile, ScaleParameter scaleParameter) {
		Assert.notNull(scaleParameter, "Parameter 'scaleParameter' can not be null!");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcImgFile);
			out = new FileOutputStream(destImgFile);
			ImageWrapper imageWrapper = ImageReadHelper.read(in);
			ScaleRender scaleRender = new ScaleRender(imageWrapper, scaleParameter);
			WriteRender writeRender = new WriteRender(scaleRender, out);
			writeRender.render();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void main(String[] args) {
		ScaleParameter scaleParameter = new ScaleParameter();
		scaleParameter.setMaxWidth(450);
		scaleImage("d:/iphone7.jpg", "d:/iphone7-scale.jpg", scaleParameter);
	}

	public static class Dimension {
		
		private Integer width;
		
		private Integer height;

		public Dimension(Integer width, Integer height) {
			super();
			this.width = width;
			this.height = height;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

		public String toString() {
			return "Dimension [width=" + width + ", height=" + height + "]";
		}
		
	}
	
}
