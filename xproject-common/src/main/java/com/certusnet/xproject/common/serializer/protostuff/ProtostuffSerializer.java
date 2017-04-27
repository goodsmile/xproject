package com.certusnet.xproject.common.serializer.protostuff;

import com.certusnet.xproject.common.serializer.ObjectSerializer;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
/**
 * 基于protostuff的对象序列化
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月14日 下午1:29:20
 * @version  	1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProtostuffSerializer implements ObjectSerializer {

	private static final Schema schema = (Schema) RuntimeSchema.getSchema(ObjectWrapper.class);
	
	public byte[] serialize(Object object) {
		if(object == null){
			return null;
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			//return ProtobufIOUtil.toByteArray(new ObjectWrapper(object), schema, buffer);
			return ProtostuffIOUtil.toByteArray(new ObjectWrapper(object), schema, buffer);
		} finally {
			buffer.clear();
		}
	}

	public <T> T deserialize(byte[] bytes) {
		if(bytes == null || bytes.length == 0){
			return null;
		}
		try {
			ObjectWrapper objectWrapper = new ObjectWrapper();
			//ProtobufIOUtil.mergeFrom(bytes, objectWrapper, schema);
			ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, schema);
			return (T) objectWrapper.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}