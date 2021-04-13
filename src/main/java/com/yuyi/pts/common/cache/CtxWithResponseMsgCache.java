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
public class CtxWithResponseMsgCache {

    public static Map<String, Object> ctxAndDataMap = new ConcurrentHashMap<>();
}
