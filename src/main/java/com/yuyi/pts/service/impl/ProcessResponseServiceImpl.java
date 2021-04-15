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

