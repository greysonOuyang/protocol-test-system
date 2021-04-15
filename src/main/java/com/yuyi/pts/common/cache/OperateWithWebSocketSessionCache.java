package com.yuyi.pts.common.cache;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地WebSocketSession管理器
 * 
 * @author <a href="http://mirrentools.org">Mirren</a>
 *
 */
public class OperateWithWebSocketSessionCache {
	/** 数据 */
	private static Map<String, WebSocketSession> SOCKET_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加一个Socket,如果已经存在Socket就替换
	 * 
	 * @param key
	 *          Socket的id通常对应 WebSocket的写id
	 * @param socket
	 * @return 如果参数key或参数socket==null则返回null
	 */
	public static WebSocketSession put(String key, WebSocketSession socket) {
		if (key == null || socket == null) {
			return null;
		}
		WebSocketSession result = SOCKET_MAP.put(key, socket);
		return result;
	}

	/**
	 * 添加一个Socket,如果已经存在Socket就返回以存在的
	 * 
	 * @param key
	 *          Socket的id通常对应 WebSocket的写id
	 * @param socket
	 * @return 如果参数key或参数socket==null则返回null
	 */
	public static WebSocketSession putIfAbsent(String key, WebSocketSession socket) {
		if (key == null || socket == null) {
			return null;
		}
		WebSocketSession result = SOCKET_MAP.putIfAbsent(key, socket);
		return result;
	}

	/**
	 * 获取Socket
	 * 
	 * @param key
	 *          Socket的id通常对应 WebSocket的写id
	 * @return 如果参数key==null则返回null
	 */
	public static WebSocketSession get(String key) {
		if (key == null) {
			return null;
		}
		return SOCKET_MAP.get(key);
	}

	/**
	 * 获取Socket
	 * 
	 * @param key
	 *          Socket的id通常对应 WebSocket的写id
	 * @param defaultValue
	 *          如果为空就返回默认值
	 * @return 如果参数key==null则返回null
	 */
	public static WebSocketSession get(String key, WebSocketSession defaultValue) {
		if (key == null) {
			return null;
		}
		return SOCKET_MAP.getOrDefault(key, defaultValue);
	}

	/**
	 * 删除Socket
	 * 
	 * @param key
	 *          Socket的id通常对应 WebSocket的写id
	 * @return 如果参数key==null则返回null
	 */
	public static WebSocketSession remove(String key) {
		if (key == null) {
			return null;
		}
		return SOCKET_MAP.remove(key);
	}

}
