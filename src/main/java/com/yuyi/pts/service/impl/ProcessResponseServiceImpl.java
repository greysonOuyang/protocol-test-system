package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SendMsg2UserUtil;
import com.yuyi.pts.common.vo.response.ResponseInfo;
import com.yuyi.pts.service.ProcessResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * 处理响应信息，即发送消息给前端
 *
 * @author greyson
 * @since 2021/4/14
 */
@Service
@Slf4j
public class ProcessResponseServiceImpl implements ProcessResponseService {

    @Override
    public void receiveDataAndSend2User(WebSocketSession session, Object msg) {
        String jsonString = JSONObject.toJSONString(msg);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setState(0);
        responseInfo.setBody(jsonString);
        String responseData = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
        log.info("客户端收到服务端的数据： {}", responseData);
        SendMsg2UserUtil.sendTextMsg(session, responseData);
    }
}

