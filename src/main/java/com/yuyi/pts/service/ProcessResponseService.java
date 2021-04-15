package com.yuyi.pts.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/14
 */
public interface ProcessResponseService {

    /**
     * 响应消息到前端
     *
     * @param session websocket会话
     * @param msg 响应信息
     */
    void receiveDataAndSend2User(WebSocketSession session, Object msg);
}
