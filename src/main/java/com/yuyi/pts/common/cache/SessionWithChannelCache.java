package com.yuyi.pts.common.cache;

import java.nio.channels.SocketChannel;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket会话与Netty的SocketChannel进行绑定，辅助 Netty拿到数据
 *
 * @author greyson
 * @since 2021/4/13
 */
public class SessionWithChannelCache {

    private static Map<WebSocketSession, SocketChannel> sessionSocketChannelMap = new ConcurrentHashMap<>();

    public static SocketChannel put(WebSocketSession session, SocketChannel channel) {
        if (session == null || channel == null) {
            return null;
        } else {
          return sessionSocketChannelMap.put(session, channel);
        }
    }

    public static SocketChannel get(WebSocketSession session) {
        if (session == null) {
            return null;
        } else {
           return sessionSocketChannelMap.get(session);
        }
    }
}
