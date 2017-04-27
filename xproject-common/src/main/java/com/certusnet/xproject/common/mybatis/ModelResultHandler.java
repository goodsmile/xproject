package com.certusnet.xproject.common.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * 用于对查询结果集的每行数据部分字段进行转义的ResultHandler
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年8月1日 下午3:54:19
 * @version  	1.0
 */
@SuppressWarnings("unchecked")
public class ModelResultHandler<T> implements ResultHandler<T> {
	
	private final List<T> list;

	private ModelHandler<T> modelHandler;
	
	public ModelResultHandler() {
		list = new ArrayList<T>();
	}
	
	public ModelResultHandler(ModelHandler<T> modelHandler) {
		this();
		this.modelHandler = modelHandler;
	}

	public ModelResultHandler(ObjectFactory objectFactory) {
		list = objectFactory.create(List.class);
	}

	
	public void handleResult(ResultContext<? extends T> context) {
		if(context.getResultCount() > 0){
    		T element = (T) context.getResultObject();
            try {
                list.add(element);
            } finally {
                if (modelHandler != null) {
                    modelHandler.handleModel(element);
                }
            }
    	}
	}

	public List<T> getResultList() {
		return list;
	}
}
