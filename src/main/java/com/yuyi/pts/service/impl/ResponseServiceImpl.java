package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.service.ResponseService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * 发送数据到前端
 *
 * @author greyson
 * @since 2021/4/16
 */
@Service
@Slf4j
public class ResponseServiceImpl implements ResponseService {

    @Override
    public void sendTextMsg(WebSocketSession session, String result) {
        try {
            session.sendMessage(new TextMessage(result));
        } catch (IOException e) {
            log.error("Session--{}发送消息失败", session.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void sendTextMsg(ChannelHandlerContext ctx, String result) {
        WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
        try {
            session.sendMessage(new TextMessage(result));
        } catch (IOException e) {
            log.error("Session--{}发送消息失败", session.getId());
            e.printStackTrace();
        }
    }
}
