package com.certusnet.xproject.common.support;

public interface SpringTypedBeanManager<T,P> {

	public T getTypedBean(P parameter);
	
}
