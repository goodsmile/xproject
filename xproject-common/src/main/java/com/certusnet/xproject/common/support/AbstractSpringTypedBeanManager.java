package com.certusnet.xproject.common.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.certusnet.xproject.common.util.CollectionUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractSpringTypedBeanManager<T,P> implements SpringTypedBeanManager<T,P>, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	private Class<T> typeOf;
	
	private volatile boolean loaded = false;
	
	private final Map<String,T> typeOfMap = new HashMap<String,T>();
	
	public AbstractSpringTypedBeanManager() {
		Class<?> c = getThisSuperclass(getClass());
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] ps = ((ParameterizedType) t).getActualTypeArguments();
            Type p = ps[0];
            if(p instanceof ParameterizedType){
            	this.typeOf = (Class<T>) ((ParameterizedType) p).getRawType();
            }else{
            	this.typeOf = (Class<T>) ps[0];
            }
        }
	}
	
	protected Class<T> getTypeOf() {
		return typeOf;
	}
	
	protected Class<?> getThisSuperclass(Class<?> clazz) {
		if(clazz.getSuperclass().equals(AbstractSpringTypedBeanManager.class)){
			return clazz;
		}else{
			return getThisSuperclass(clazz.getSuperclass());
		}
	}
	
	public T getTypedBean(P parameter) {
		if(!loaded){
			synchronized (typeOfMap) {
				if(!loaded){
					Map<String,T> beans = applicationContext.getBeansOfType(getTypeOf());
					if(!CollectionUtils.isEmpty(beans)){
						for(Map.Entry<String,T> entry : beans.entrySet()){
							typeOfMap.put(entry.getKey(), processBean(entry.getValue()));
						}
					}
				}
			}
			loaded = true;
		}
		for(Map.Entry<String,T> entry : typeOfMap.entrySet()){
			T bean = entry.getValue();
			if(filterBean(bean, parameter)){
				return bean;
			}
		}
		noBeanFound(parameter); //no bean found
		return null;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//如果参数applicationContext是容器环境下SpringMVC Application上下文,则取其parent (ROOT)
		if(applicationContext instanceof WebApplicationContext && applicationContext.getParent() != null){ 
    		this.applicationContext = applicationContext.getParent();
    	}
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected T processBean(T bean) {
		return bean;
	}
	
	protected void noBeanFound(P parameter) {
		
	}
	
	protected abstract boolean filterBean(T bean, P parameter);
	
}
