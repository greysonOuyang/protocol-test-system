package com.yuyi.pts.common.cache;

import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wzl
 * @date : 2021/4/16/16:15
 * @description:
 */
public class xxObj {
    public static Map<String, ByteBuf> map  = new ConcurrentHashMap<>();

    public static ByteBuf get(String ctx) {
        if (ctx == null) {
            return null;
        } else {
            return map.get(ctx);
        }
    }

    public static Object put(String ctx, ByteBuf msg) {
        if (ctx == null || msg == null) {
            return null;
        } else {
            return map.put(ctx, msg);
        }
    }
}
