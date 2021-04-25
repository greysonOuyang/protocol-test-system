package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.model.vo.request.RequestMainDTO;
import com.yuyi.pts.service.ExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * 前端与后端进行websocket通信的处理 所有请求的处理入口
 * 这里列出大方向的 TODO List
 *  1.批量请求数据
 *  2. 往服务端发送byteBuf或者二进制或者json或者字符串 默认都是用bytebuf通信、所以需要使用各种codec
 *  3. 各个协议的粘包、拆包问题
 *
 *
 *
 * @author greyson
 * @since 2021/4/12
 */
@Component
@Slf4j
public class HandlerDispatcher extends AbstractWebSocketHandler {

    @Autowired
    private ExecuteService executeService;


    /**
     * 建立连接后
     *
     * @param session session
     * @throws Exception ex
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("websockt连接建立了");
        super.afterConnectionEstablished(session);
    }


    /**
     * 处理客户端发来的消息，根据消息指令分发到对应的处理方法
     *
     * @param session session
     * @param message 消息体
     * @throws Exception ex
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Object payload = message.getPayload();
        JSONObject jsonObject = JSON.parseObject((String) payload);
        RequestMainDTO requestMainDTO = JSON.toJavaObject(jsonObject, RequestMainDTO.class);
        log.info("receive client request：" + jsonObject.toString());
        int code = requestMainDTO.getCode();
        RequestDataDto data = requestMainDTO.getData();
        Object body = data.getBody();
        String content = JSON.toJSONString(body);
//        String content = (String) body;
        if (log.isDebugEnabled()) {
            log.debug("receive client message:" + content);
        }
        if (code == OperationCommand.CANCEL.value()) {
            // 关闭连接
            session.close();
        } else if(code == OperationCommand.SUBMIT_TEST.value()) {
            executeService.execute(session, data);
        }
    }

    /**
     * 连接关闭后
     *
     * @param session session
     * @param status 关闭状态
     * @throws Exception ex
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
