package com.yuyi.pts.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.enums.OptionCommand;
import com.yuyi.pts.common.vo.request.DataContentDTO;
import com.yuyi.pts.common.vo.request.RequestDTO;
import com.yuyi.pts.websocket.WsSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Component
@Slf4j
public class HandlerDispatcher extends AbstractWebSocketHandler {

    /**
     * 建立连接后
     *
     * @param session session
     * @throws Exception ex
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        super.afterConnectionEstablished(session);
    }


    /**
     * 处理客户端发来的消息
     *
     * @param session session
     * @param message 消息体
     * @throws Exception ex
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        RequestDTO requestDTO = (RequestDTO) message;
        DataContentDTO dataContent = requestDTO.getData();
        String contentJson = JSON.toJSONString(dataContent);

        if (log.isDebugEnabled()) {
            log.debug("receive user request:" + contentJson);
        }
        Integer code = requestDTO.getCode();
        if (code.equals(OptionCommand.CANCEL.value())) {
            // 关闭连接
        }
    }


}
