package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.*;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.enums.SslCertType;
import com.yuyi.pts.common.util.JvmMetricsUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SendMsg2UserUtil;
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
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());


    /**
     * 请求类型
     */
    private RequestType requestType;

    @Override
    public void execute(WebSocketSession session, RequestDataDto requestDataDto) {
        boolean isSuccess = checkOperattion(session, requestDataDto);
        if (isSuccess) {
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    JSONObject result = new JSONObject();
                    result.put("processors", JvmMetricsUtil.availableProcessors());
                    result.put("totalMemory", JvmMetricsUtil.totalMemory());
                    result.put("maxMemory", JvmMetricsUtil.maxMemory());
                    result.put("freeMemory", JvmMetricsUtil.freeMemory());
                    if (log.isDebugEnabled()) {
                        log.info("执行发送信息给客户端-->当前服务器性能:{}", result);
                    }
                    String jsonResult = successWithData(OperationCommand.JVM_METRIC.value(), result);
                    SendMsg2UserUtil.sendTextMsg(session, jsonResult);
                }
                // 测试时先关闭
            }, 0, 3600000, TimeUnit.MILLISECONDS);
            startTest(session, requestDataDto);

            // TODO 把数据放入缓存
            // TODO 设置Socket关闭事件
        } else {
            log.info("请求参数校验失败");
            String responseMsg = ResultEntity.failedWithoutNothing(OperationCommand.MISSING_PARAMETER.value());
            SendMsg2UserUtil.sendTextMsg(session, responseMsg);
        }
    }

    private boolean checkOperattion(WebSocketSession session, RequestDataDto dataContent) {
        if (log.isDebugEnabled()) {
            log.info("执行参数检查并加载请求信息，请求数据为--》{}", JSON.toJSONString(dataContent));
        }
        requestType = dataContent.getType();
        SslCertType cert = dataContent.getCert();
//        if (cert != null && cert != SslCertType.DEFAULT) {
//            if (SslCertType.PFX == cert) {
//                // TODO 证书校验
//            }
//        }

        // 往下是对输入的参数合法性校验 也许不需要
        if (requestType == RequestType.HTTP) {
           return checkHttpRequest(session,dataContent);
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
     *
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
    private boolean checkHttpRequest(WebSocketSession session,RequestDataDto dataContent) {
        boolean flag = false;
        // TODO 参数校验
        // 把url的字符串进行截取，拼接host和port
        String url = dataContent.getUrl();
        String target = url.substring(0,7);
        // 校验前6位
        if("http://".equals(target)){
            flag=true;
        }else if ("https:/".equals(target)){
            flag=true;
        }else {
            String responseData = ResultEntity.failedWithMsg(OperationCommand.MISSING_PARAMETER.value(), "请输入正确的Ip地址");
            try {
                session.sendMessage(new TextMessage(responseData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 真正执行测试的地方
     *
     * @param session     会话
     * @param requestDataDto 数据
     */
    private void startTest(WebSocketSession session, RequestDataDto requestDataDto) {
        // TODO 对请求进行校验 校验通过则根据protocolHandlerDispatcher进行任务分发，校验失败返回false到此处
        String host = requestDataDto.getHost();
        Integer port = requestDataDto.getPort();
        String operateId = UUID.randomUUID().toString();
        String sessionId = session.getId();
        requestDataDto.setId(operateId);
        requestDataDto.setId(session.getId());
        // 存储需要请求的数量
        LocalDataCounter.newCounter(operateId, ((long) requestDataDto.getAverage() * requestDataDto.getCount()));
        // 共享WebSocketSession
        OperateWithWebSocketSessionCache.put(operateId, session);
        // 共享请求配置
        OperateIdWithRequestDtoCache.put(operateId, requestDataDto);
     //   protocolHandlerDispatcher.submitRequest(session, host, port, requestType, requestDataDto);
        if("TCP".equals(requestType.name())){
            protocolHandlerDispatcher.submitRequest(session, host, port, requestType, requestDataDto);
        }else if("HTTP".equals(requestType.name())){
            protocolHandlerDispatcher.submitHttpRequest(session, requestType,requestDataDto);
        }

        System.out.println("netty启动完成，执行了此处");

    }



}
