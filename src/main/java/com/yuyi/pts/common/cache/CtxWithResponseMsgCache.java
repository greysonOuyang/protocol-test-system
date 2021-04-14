package com.yuyi.pts.common.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelHandlerContext和服务端返回的Msg进行绑定的缓存，辅助客户端取到接口系统返回的值
 *
 * @author greyson
 * @since 2021/4/13
 */
public class CtxWithResponseMsgCache {

    public static Boolean isDataReady = false;

    public static Map<ChannelHandlerContext, Object> ctxAndDataMap = new ConcurrentHashMap<>();

    public static Object get(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        } else {
           return ctxAndDataMap.get(ctx);
        }
    }

    public static Object put(ChannelHandlerContext ctx, Object msg) {
        if (ctx == null || msg == null) {
            return null;
        } else {
            return ctxAndDataMap.put(ctx, msg);
        }
    }
}
