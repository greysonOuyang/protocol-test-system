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
     *
     * @param session
     * @param msg
     */
    void receiveDataAndSend2User(WebSocketSession session, Object msg);
}
