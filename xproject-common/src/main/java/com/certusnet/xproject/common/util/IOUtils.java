package com.certusnet.xproject.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * I/O流的相关工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月20日 上午12:33:00
 * @version  	1.0
 */
public class IOUtils {

    /**
     * <p>关闭流(不抛异常)</p>
     * 
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>关闭流</p>
     * 
     * @param closeable
     * @throws IOException
     */
    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

}
