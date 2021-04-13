package com.yuyi.pts.common.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/13
 */
public class SessionIdWithRequestDataCache {

    public static Map<String, ChannelHandlerContext> ctxAndDataMap = new ConcurrentHashMap<>();
}
