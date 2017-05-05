package com.certusnet.xproject.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.certusnet.xproject.common.support.ImagePixel;

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
	public static ImagePixel getImagePixel(String fullImgPath) {
		return getImagePixel(new File(fullImgPath));
	}
	
	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static ImagePixel getImagePixel(File imgFile) {
		try {
			return getImagePixel(new FileInputStream(imgFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static ImagePixel getImagePixel(InputStream in) {
		try {
			ImageWrapper imageWrapper = ImageReadHelper.read(in);
			return new ImagePixel(imageWrapper.getWidth(), imageWrapper.getHeight());
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
		scaleParameter.setMaxWidth(300);
		scaleParameter.setMaxHeight(300);
		scaleImage("d:/iphone7.jpg", "d:/iphone7-scale.jpg", scaleParameter);
	}

}
