package com.yuyi.pts.service;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.socket.WebSocketSession;

/**
 * 处理响应信息，即发送消息给前端
 *
 * @author greyson
 * @since 2021/4/14
 */
public interface ResponseService {

    /**
     * 发送信息
     *
     * @param session
     * @param result
     */
    void sendTextMsg(WebSocketSession session, String result);

    /**
     * 发送信息
     *
     * @param ctx
     * @param result
     */
    void sendTextMsg(ChannelHandlerContext ctx, String result);

}
