package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.service.ProcessResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/14
 */
@Service
@Slf4j
public class ProcessResponseServiceImpl implements ProcessResponseService {

    @Override
    public void receiveDataAndSend2User(WebSocketSession session, Object msg) {
//        String id = session.getId();
//        log.info("接收数据时的SessionId是：{}", id);
//        // TODO 发送数据问题
//        Object responseData = null;
//
//        ChannelHandlerContext ctx = CtxWithSessionIdCache.get(id);
//        log.info("CtxWithSessionIdCache缓存的获取结果：key--{}, value--{}", id, ctx.hashCode());
//        responseData = CtxWithResponseMsgCache.get(ctx);
//        log.info("CtxWithResponseMsgCache缓存的获取结果：key--{}, value--{}", ctx.hashCode(), responseData);


        String result = JSON.toJSONString(msg);
        log.info("客户端收到服务端的数据： {}", msg);
        String responseData = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE.value(), result);
        log.info("客户端收到服务端的数据2： {}", responseData);
        log.info("客户端将服务端收到的数据转成json:{}", responseData);
        try {
            synchronized (session) {
                session.sendMessage(new TextMessage(responseData));
            }
        } catch (IOException e) {
            log.error("发送数据到用户的时候出错{}", e.getMessage());
            e.printStackTrace();
        }
    }
}

