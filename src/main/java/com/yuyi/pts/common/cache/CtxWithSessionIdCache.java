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
public class CtxWithSessionIdCache {
    /** 数据 */
    public static Map<String, ChannelHandlerContext> sessionAndCtxMap = new ConcurrentHashMap<>();

    public static ChannelHandlerContext put(String key, ChannelHandlerContext ctx) {
        if (key == null || ctx == null) {
            return null;
        }
        return sessionAndCtxMap.put(key, ctx);
    }
}
