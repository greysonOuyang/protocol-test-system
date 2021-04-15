package com.yuyi.pts.common.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wzl
 * @date : 2021/4/15/10:30
 * @description:
 */
public class ObjCache {
    public static Map<String, String> map  = new ConcurrentHashMap<>();

    public static Object get(String ctx) {
        if (ctx == null) {
            return null;
        } else {
            return map.get(ctx);
        }
    }

    public static Object put(String ctx, String msg) {
        if (ctx == null || msg == null) {
            return null;
        } else {
            return map.put(ctx, msg);
        }
    }
}
