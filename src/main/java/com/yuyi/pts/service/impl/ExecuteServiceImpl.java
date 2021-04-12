package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.JvmMetricsUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.service.ExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.yuyi.pts.common.util.ResultEntity.successWithData;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {


    @Autowired
    private NettyClient nettyClient;

    @Override
    public void execute(WebSocketSession session,  RequestDataDto dataContent) {

        JSONObject result = new JSONObject();
        result.put("processors", JvmMetricsUtil.availableProcessors());
        result.put("totalMemory", JvmMetricsUtil.totalMemory());
        result.put("maxMemory", JvmMetricsUtil.maxMemory());
        result.put("freeMemory", JvmMetricsUtil.freeMemory());
        log.info("执行发送信息给客户端-->当前服务器性能:" + result);
        ResultEntity<JSONObject> jsonObjectResultEntity = successWithData(result);
        String metrix = JSON.toJSONString(jsonObjectResultEntity);
        try {
            session.sendMessage(new TextMessage(metrix));
            startTest(session, dataContent);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 真正执行测试的地方
     *
     * @param session
     * @param dataContent
     */
    private void startTest(WebSocketSession session, RequestDataDto dataContent) {
        String host = dataContent.getHost();
        Integer port = dataContent.getPort();
        // TODO SSL证书校验
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        nettyClient.start();
    }

}
