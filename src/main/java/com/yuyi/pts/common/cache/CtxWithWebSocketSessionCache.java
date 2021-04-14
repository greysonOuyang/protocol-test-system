package com.yuyi.pts.common.cache;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wzl
 * @date : 2021/4/14/15:47
 * @description:
 */
public class CtxWithWebSocketSessionCache {
    /** 数据 */
    public static Map<ChannelHandlerContext, WebSocketSession> cxtWithSessionMap = new ConcurrentHashMap<>();


    /**
     * 添加一个WebSocketSession,如果已经存在WebSocketSession就替换
     *
     * @param key
     *          WebSocketSession的id通常对应 WebSocket的写id
     * @param ctx
     * @return 如果参数key或参数options==null则返回null
     */
    public static WebSocketSession put(ChannelHandlerContext key, WebSocketSession ctx) {
        if (key == null || ctx == null) {
            return null;
        }
        return cxtWithSessionMap.put(key, ctx);
    }

    /**
     * 添加一个WebSocketSession,如果已经存在WebSocketSession就返回以存在的
     *
     * @param key
     *          WebSocketSession的id通常对应 WebSocket的写id
     * @param ctx
     * @return 如果参数key或参数options==null则返回null
     */
    public static WebSocketSession putIfAbsent(ChannelHandlerContext key, WebSocketSession ctx) {
        if (key == null || ctx == null) {
            return null;
        }
        WebSocketSession result = cxtWithSessionMap.putIfAbsent(key, ctx);
        return result;
    }

    /**
     * 获取WebSocketSession
     *
     * @param key
     *          WebSocketSession的id通常对应 WebSocket的写id
     * @return 如果参数key==null则返回null
     */
    public static WebSocketSession get(ChannelHandlerContext key) {
        if (key == null) {
            return null;
        }
        return cxtWithSessionMap.get(key);
    }

    /**
     * 获取WebSocketSession
     *
     * @param key
     *          WebSocketSession的id通常对应 WebSocket的写id
     * @param defaultValue
     *          如果为空就返回默认值
     * @return 如果参数key==null则返回null
     */
    public static WebSocketSession get(ChannelHandlerContext key, WebSocketSession defaultValue) {
        if (key == null) {
            return null;
        }
        return cxtWithSessionMap.getOrDefault(key, defaultValue);
    }

    /**
     * 删除WebSocketSession
     *
     * @param key
     *          WebSocketSession的id通常对应 WebSocket的写id
     * @return 如果参数key==null则返回null
     */
    public static WebSocketSession remove(ChannelHandlerContext key) {
        if (key == null) {
            return null;
        }
        return cxtWithSessionMap.remove(key);
    }
}
