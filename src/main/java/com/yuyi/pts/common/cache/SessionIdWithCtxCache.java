package com.yuyi.pts.common.cache;


import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty的channelHandlerContent和session进行绑定的缓存；作用是客户端可以根据sessionId拿到对应的ctx，
 * 辅助客户端依靠ctx去SessionIdWithRequestDataCache拿到服务端返回的数据，然后将数据返回前端
 *
 * @author greyson
 * @since 2021/4/13
 */
public class SessionIdWithCtxCache {
    /** 数据 */
    public static Map<String, ChannelHandlerContext> sessionAndCtxMap = new ConcurrentHashMap<>();


    /**
     * 添加一个ChannelHandlerContext,如果已经存在ChannelHandlerContext就替换
     *
     * @param key
     *          ChannelHandlerContext的id通常对应 WebSocket的写id
     * @param ctx
     * @return 如果参数key或参数options==null则返回null
     */
    public static ChannelHandlerContext put(String key, ChannelHandlerContext ctx) {
        if (key == null || ctx == null) {
            return null;
        }
        return sessionAndCtxMap.put(key, ctx);
    }

    /**
     * 添加一个ChannelHandlerContext,如果已经存在ChannelHandlerContext就返回以存在的
     *
     * @param key
     *          ChannelHandlerContext的id通常对应 WebSocket的写id
     * @param ctx
     * @return 如果参数key或参数options==null则返回null
     */
    public static ChannelHandlerContext putIfAbsent(String key, ChannelHandlerContext ctx) {
        if (key == null || ctx == null) {
            return null;
        }
        ChannelHandlerContext result = sessionAndCtxMap.putIfAbsent(key, ctx);
        return result;
    }

    /**
     * 获取ChannelHandlerContext
     *
     * @param key
     *          ChannelHandlerContext的id通常对应 WebSocket的写id
     * @return 如果参数key==null则返回null
     */
    public static ChannelHandlerContext get(String key) {
        if (key == null) {
            return null;
        }
        return sessionAndCtxMap.get(key);
    }

    /**
     * 获取ChannelHandlerContext
     *
     * @param key
     *          ChannelHandlerContext的id通常对应 WebSocket的写id
     * @param defaultValue
     *          如果为空就返回默认值
     * @return 如果参数key==null则返回null
     */
    public static ChannelHandlerContext get(String key, ChannelHandlerContext defaultValue) {
        if (key == null) {
            return null;
        }
        return sessionAndCtxMap.getOrDefault(key, defaultValue);
    }

    /**
     * 删除ChannelHandlerContext
     *
     * @param key
     *          ChannelHandlerContext的id通常对应 WebSocket的写id
     * @return 如果参数key==null则返回null
     */
    public static ChannelHandlerContext remove(String key) {
        if (key == null) {
            return null;
        }
        return sessionAndCtxMap.remove(key);
    }
}
