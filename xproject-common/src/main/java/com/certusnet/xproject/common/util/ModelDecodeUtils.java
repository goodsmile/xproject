package com.certusnet.xproject.common.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 数据模型中字段值解码
 * 例如将字段中的 typeCode=1进行解码转义得到typeName="字面意义"
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午4:45:57
 * @version 	1.0
 */
public class ModelDecodeUtils {

	public static <T> T decodeModel(T model, Consumer<T> consumer) {
		Optional.ofNullable(model).ifPresent(consumer);
		return model;
	}

	public static <T> List<T> decodeModel(List<T> models, Consumer<T> consumer) {
		Optional.ofNullable(models).ifPresent(roles -> {
			roles.stream().forEach(consumer);
		});
		return models;
	}
	
}
