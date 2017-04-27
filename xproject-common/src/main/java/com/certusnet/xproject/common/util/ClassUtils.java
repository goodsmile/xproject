package com.certusnet.xproject.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * 有关Class的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午4:00:20
 * @version  	1.0
 */
@SuppressWarnings("unused")
public class ClassUtils {

	/** Suffix for class, e.g. class java.lang.String */
	public static final String CLASS_TYPE_TOSTRING_SUFFIX = "class ";
	
	/** Suffix for class, e.g. interface java.util.List */
	public static final String INTERFACE_TYPE_TOSTRING_SUFFIX = "interface ";
	
	/** Suffix for array class names: "[]" */
	public static final String ARRAY_SUFFIX = "[]";

	/** Prefix for internal array class names: "[" */
	private static final String INTERNAL_ARRAY_PREFIX = "[";

	/** Prefix for internal non-primitive array class names: "[L" */
	private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

	/** The package separator character '.' */
	private static final char PACKAGE_SEPARATOR = '.';

	/** The inner class separator character '$' */
	private static final char INNER_CLASS_SEPARATOR = '$';

	/** The CGLIB class separator character "$$" */
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	/** The ".class" file suffix */
	public static final String CLASS_FILE_SUFFIX = ".class";


	/**
	 * 基本类型名字的包装类型到原始类型的映射
	 * for example: Integer.class -> int.class.
	 */
	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

	/**
	 * 基本类型名字的原始类型到包装类型的映射
	 * for example: int.class -> Integer.class.
	 */
	private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);

	/**
	 * 基本类型名字的String类型到Class<?>类型的映射
	 * for example: "int" -> "int.class".
	 */
	private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);

	/**
	 * Map with common "java.lang" class name as key and corresponding Class as value.
	 * Primarily for efficient deserialization of remote invocations.
	 */
	private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);


	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);

		for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
			primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
			registerCommonClasses(entry.getKey());
		}

		Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(32);
		primitiveTypes.addAll(primitiveWrapperTypeMap.values());
		primitiveTypes.addAll(Arrays.asList(new Class<?>[] {
				boolean[].class, byte[].class, char[].class, double[].class,
				float[].class, int[].class, long[].class, short[].class}));
		primitiveTypes.add(void.class);
		for (Class<?> primitiveType : primitiveTypes) {
			primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
		}

		registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
				Float[].class, Integer[].class, Long[].class, Short[].class);
		registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
				Object.class, Object[].class, Class.class, Class[].class);
		registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
				Error.class, StackTraceElement.class, StackTraceElement[].class);
	}
	
	/**
	 * <p>注册一些常用的公共的class到缓存中去</p>
	 * 
	 * @param commonClasses
	 */
	private static void registerCommonClasses(Class<?>... commonClasses) {
		for (Class<?> clazz : commonClasses) {
			commonClassCache.put(clazz.getName(), clazz);
		}
	}
	
	/**
	 * <p>获取系统默认的ClassLoader</p>
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtils.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	 * <p>根据字符串形式的class表示形式获取其Class<?>类型</p>
	 * 
	 * <pre>
	 * for example: "int"					-> 		Integer.class
	 * 				"java.util.Date"		->		Date.class
	 * 				"class java.util.Date"	->		Date.class
	 * 				"interface java.util.List"	->	List.class
	 * 				"java.lang.String[]"	->		String[].class
	 * 				"[Ljava.lang.String;"	->		String[].class
	 * 				"[[Ljava.lang.String;"	->		String[][].class
	 * 				"java.lang.Thread.State"->		Thread.State.class
	 * 				"java.lang.Thread$State"->		Thread.State.class
	 * </pre>
	 * 
	 * @param name
	 * @param classLoader
	 * @return
	 * @throws ClassNotFoundException
	 * @throws LinkageError
	 */
	public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
		Assert.notNull(name, "Name must not be null");

		Class<?> clazz = resolvePrimitiveClassName(name);
		if (clazz == null) {
			clazz = commonClassCache.get(name);
		}
		if (clazz != null) {
			return clazz;
		}

		if(name.startsWith(CLASS_TYPE_TOSTRING_SUFFIX)){ // strip start "class "
			name = name.substring(CLASS_TYPE_TOSTRING_SUFFIX.length()).trim();
		}else if(name.startsWith(INTERFACE_TYPE_TOSTRING_SUFFIX)){ //strip start "interface "
			name = name.substring(INTERFACE_TYPE_TOSTRING_SUFFIX.length()).trim();
		}
		
		// "java.lang.String[]" style arrays
		if (name.endsWith(ARRAY_SUFFIX)) {
			String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			Class<?> elementClass = forName(elementClassName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}

		// "[Ljava.lang.String;" style arrays
		if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
			String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
			Class<?> elementClass = forName(elementName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}

		// "[[I" or "[[Ljava.lang.String;" style arrays
		if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
			String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
			Class<?> elementClass = forName(elementName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}

		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = getDefaultClassLoader();
		}
		try {
			return classLoaderToUse.loadClass(name);
		}
		catch (ClassNotFoundException ex) {
			int lastDotIndex = name.lastIndexOf('.');
			if (lastDotIndex != -1) {
				String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
				try {
					return classLoaderToUse.loadClass(innerClassName);
				}
				catch (ClassNotFoundException ex2) {
					// swallow - let original exception get through
				}
			}
			throw ex;
		}
	}

	/**
	 * <p>根据基本类型的String形式得到其Class<?>类型</p>
	 * 
	 * <pre>
	 * for example: "int" -> int.class
	 * </pre>
	 * 
	 * @param name
	 * @return
	 */
	public static Class<?> resolvePrimitiveClassName(String name) {
		Class<?> result = null;
		// Most class names will be quite long, considering that they
		// SHOULD sit in a package, so a length check is worthwhile.
		if (name != null && name.length() <= 8) {
			// Could be a primitive - likely.
			result = primitiveTypeNameMap.get(name);
		}
		return result;
	}
	
	/**
	 * <p>判断baseType是否是subType的父类或本身</p>
	 * 
	 * @param baseType
	 * @param subType
	 * @return
	 */
	public static boolean isAssignable(Class<?> baseType, Class<?> subType) {
		Assert.notNull(baseType, "baseType must not be null");
		Assert.notNull(subType, "subType must not be null");
		if (baseType.isAssignableFrom(subType)) {
			return true;
		}
		if (baseType.isPrimitive()) {
			Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(subType);
			if (resolvedPrimitive != null && baseType.equals(resolvedPrimitive)) {
				return true;
			}
		}
		else {
			Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(subType);
			if (resolvedWrapper != null && baseType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>判断baseType是否是subTypeValue对应类型的父类或本身</p>
	 * 
	 * @param baseType
	 * @param subTypeValue
	 * @return
	 */
	public static boolean isAssignableValue(Class<?> baseType, Object subTypeValue) {
		Assert.notNull(baseType, "baseType must not be null");
		return (subTypeValue != null ? isAssignable(baseType, subTypeValue.getClass()) : !baseType.isPrimitive());
	}
	
	/**
	 * <p>检测参数object是否是一个cglib的代理对象</p>
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isCglibProxy(Object object) {
		return ClassUtils.isCglibProxyClass(object.getClass());
	}

	/**
	 * <p>检测参数clazz是否是一个cglib的代理对象类型</p>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isCglibProxyClass(Class<?> clazz) {
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}

	/**
	 * <p>检测参数clazzName是否是一个cglib的代理对象类型</p>
	 * 
	 * @param className
	 * @return
	 */
	public static boolean isCglibProxyClassName(String className) {
		return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
	}
	
	/**
	 * <p>根据class获取package名字</p>
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getPackageName(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return getPackageName(clazz.getName());
	}

	/**
	 * <p>根据类名获取package名字</p>
	 * 
	 * @param fqClassName
	 * @return
	 */
	public static String getPackageName(String fqClassName) {
		Assert.notNull(fqClassName, "Class name must not be null");
		int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
	}
	
}
