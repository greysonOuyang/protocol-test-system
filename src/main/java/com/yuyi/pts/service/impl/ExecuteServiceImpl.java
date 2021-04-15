package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.JvmMetricsUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import com.yuyi.pts.service.ExecuteService;
import com.yuyi.pts.service.ProtocolHandlerDispatcher;
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

    /**
     * 请求类型
     */
    private RequestType requestType;

    @Override
    public void execute(WebSocketSession session, RequestDataDto dataContent) {
        checkOperattion(session, dataContent);

        JSONObject result = new JSONObject();
        result.put("processors", JvmMetricsUtil.availableProcessors());
        result.put("totalMemory", JvmMetricsUtil.totalMemory());
        result.put("maxMemory", JvmMetricsUtil.maxMemory());
        result.put("freeMemory", JvmMetricsUtil.freeMemory());
        log.info("执行发送信息给客户端-->当前服务器性能:" + result);
        String jsonResult = successWithData(OperationCommand.JVM_METRIC.value(), result);
        try {
            session.sendMessage(new TextMessage(jsonResult));
            startTest(session, dataContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkOperattion(WebSocketSession session, RequestDataDto dataContent) {
        boolean isSuccess;
        if (log.isDebugEnabled()) {
            log.info("执行参数检查并加载请求信息，请求数据为--》{}", JSON.toJSONString(dataContent));
        }
        requestType = dataContent.getType();
        if (requestType == RequestType.HTTP) {
           return checkHttpRequest(dataContent);
        } else if (requestType == RequestType.TCP) {
           return checkTcpRequest(dataContent);

        } else if (requestType == RequestType.WebSocket) {
           return checkWebSocketRequest(dataContent);
        } else {
            return false;
        }
    }

    /**
     * 检查 TCP 请求
     *
     * @param dataContent
     * @return
     */
    private boolean checkTcpRequest(RequestDataDto dataContent) {
        return true;
        // TODO 参数校验

    }

    /**
     * 检查websocket请求
     * @param dataContent
     * @return
     */
    private boolean checkWebSocketRequest(RequestDataDto dataContent) {
        return true;
        // TODO 参数校验

    }

    /**
     * 检查Http请求
     * @param dataContent
     * @return
     */
    private boolean checkHttpRequest(RequestDataDto dataContent) {
        // TODO 参数校验
        return true;
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
        String id = session.getId();
        dataContent.setId(session.getId());
        protocolHandlerDispatcher.submitRequest(session, host, port, requestType, dataContent);
        System.out.println("netty启动完成，执行了此处");
        // TODO SSL证书校验

    }



}
