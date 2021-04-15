package com.yuyi.pts.common.cache;


import com.yuyi.pts.common.vo.request.RequestDataDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地管理器
 * 
 * @author <a href="http://mirrentools.org">Mirren</a>
 *
 */
public class OperateIdWithRequestDtoCache {
	/** 数据 */
	private static Map<String, RequestDataDto> OPTIONS_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加一个RequestOptions,如果已经存在RequestOptions就替换
	 * 
	 * @param key
	 *          RequestOptions的id通常对应 WebSocket的写id
	 * @param options
	 * @return 如果参数key或参数options==null则返回null
	 */
	public static RequestDataDto put(String key, RequestDataDto options) {
		if (key == null || options == null) {
			return null;
		}
		RequestDataDto result = OPTIONS_MAP.put(key, options);
		return result;
	}

	/**
	 * 添加一个RequestOptions,如果已经存在RequestOptions就返回以存在的
	 * 
	 * @param key
	 *          RequestOptions的id通常对应 WebSocket的写id
	 * @param options
	 * @return 如果参数key或参数options==null则返回null
	 */
	public static RequestDataDto putIfAbsent(String key, RequestDataDto options) {
		if (key == null || options == null) {
			return null;
		}
		RequestDataDto result = OPTIONS_MAP.putIfAbsent(key, options);
		return result;
	}

	/**
	 * 获取RequestOptions
	 * 
	 * @param key
	 *          RequestOptions的id通常对应 WebSocket的写id
	 * @return 如果参数key==null则返回null
	 */
	public static RequestDataDto get(String key) {
		if (key == null) {
			return null;
		}
		return OPTIONS_MAP.get(key);
	}

	/**
	 * 获取RequestOptions
	 * 
	 * @param key
	 *          RequestOptions的id通常对应 WebSocket的写id
	 * @param defaultValue
	 *          如果为空就返回默认值
	 * @return 如果参数key==null则返回null
	 */
	public static RequestDataDto get(String key, RequestDataDto defaultValue) {
		if (key == null) {
			return null;
		}
		return OPTIONS_MAP.getOrDefault(key, defaultValue);
	}

	/**
	 * 删除RequestOptions
	 * 
	 * @param key
	 *          RequestOptions的id通常对应 WebSocket的写id
	 * @return 如果参数key==null则返回null
	 */
	public static RequestDataDto remove(String key) {
		if (key == null) {
			return null;
		}
		return OPTIONS_MAP.remove(key);
	}

}
