package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.CtxWithSessionIdCache;
import com.yuyi.pts.common.cache.CtxWithResponseMsgCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.JvmMetricsUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import com.yuyi.pts.service.ExecuteService;
import com.yuyi.pts.service.ProtocolHandlerDispatcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.yuyi.pts.common.util.ResultEntity.successWithData;

/**
 * 执行测试的主类，对请求进行验证、识别，根据请求类型调用不同的协议处理service，并启动NettyClient
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {


    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private ProtocolHandlerDispatcher protocolHandlerDispatcher;

    @Autowired
    private TcpRequestHandler tcpRequestHandler;

    @Override
    public void execute(WebSocketSession session, RequestDataDto dataContent) {
        JSONObject result = new JSONObject();
        result.put("processors", JvmMetricsUtil.availableProcessors());
        result.put("totalMemory", JvmMetricsUtil.totalMemory());
        result.put("maxMemory", JvmMetricsUtil.maxMemory());
        result.put("freeMemory", JvmMetricsUtil.freeMemory());
        log.info("执行发送信息给客户端-->当前服务器性能:" + result);
        String jsonResult = ResultEntity.getJsonResult(successWithData(OperationCommand.JVM_METRIC, result));
        try {
            session.sendMessage(new TextMessage(jsonResult));
            startTest(session, dataContent);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 真正执行测试的地方
     *
     * @param session     会话
     * @param dataContent 数据
     */
    private void startTest(WebSocketSession session, RequestDataDto dataContent) {
        String host = dataContent.getHost();
        Integer port = dataContent.getPort();
        RequestType type = dataContent.getType();
        protocolHandlerDispatcher.submitRequest(session, host, port, type, dataContent);
        System.out.println("netty启动完成，执行了此处");
//        receiveData(session);
        // TODO SSL证书校验

    }



}
