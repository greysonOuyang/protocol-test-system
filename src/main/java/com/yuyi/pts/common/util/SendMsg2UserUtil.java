package com.yuyi.pts.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/15
 */
@Slf4j
public class SendMsg2UserUtil {

    public static void sendTextMsg(WebSocketSession session, String result) {
        try {
            session.sendMessage(new TextMessage(result));
        } catch (IOException e) {
            log.error("Session--{}发送消息失败", session.getId());
            e.printStackTrace();
        }
    }
}
