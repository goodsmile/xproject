package com.certusnet.xproject.common.serializer.protostuff;

public class ObjectWrapper {

	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public ObjectWrapper() {
		super();
	}

	public ObjectWrapper(Object object) {
		super();
		this.object = object;
	}

}
