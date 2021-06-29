package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.enums.ProtocolType;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.enums.SslCertType;
import com.yuyi.pts.common.util.JvmMetricsUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.initializer.*;
import com.yuyi.pts.service.ExecuteService;
import com.yuyi.pts.service.ResponseService;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
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
    private ResponseService responseService;

    private final ScheduledExecutorService scheduledExecutorService = ScheduledThreadPoolUtil.getInstance();

    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private AbstractNettyInitializer abstractNettyInitializer;

    /**
     * 请求类型
     */
    private RequestType requestType;


    @Override
    public void execute(WebSocketSession session, RequestDataDto requestDataDto) throws IOException {
        boolean isSuccess = checkOperattion(session, requestDataDto);
        if (isSuccess) {
            // 测试时先关闭
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                JSONObject result = new JSONObject();
                result.put("processors", JvmMetricsUtil.availableProcessors());
                result.put("totalMemory", JvmMetricsUtil.totalMemory());
                result.put("maxMemory", JvmMetricsUtil.maxMemory());
                result.put("freeMemory", JvmMetricsUtil.freeMemory());
                if (log.isDebugEnabled()) {
                    log.info("执行发送信息给客户端-->当前服务器性能:{}", result);
                }
                String response = successWithData(OperationCommand.JVM_METRIC, result);
                responseService.sendTextMsg(session, response);
            }, 0, 360000, TimeUnit.MILLISECONDS);
            startTest(session, requestDataDto);

            // TODO 把数据放入缓存
            // TODO 设置Socket关闭事件
        } else {
            log.info("请求参数校验失败");
            String response = ResultEntity.failedWithoutNothing(OperationCommand.MISSING_PARAMETER);
            responseService.sendTextMsg(session, response);
        }
    }


    private boolean checkOperattion(WebSocketSession session, RequestDataDto dataContent) {
        if (log.isDebugEnabled()) {
            log.info("执行参数检查并加载请求信息，请求数据为--》{}", JSON.toJSONString(dataContent));
        }
        requestType = dataContent.getType();
        // TODO 证书校验
        SslCertType cert = dataContent.getCert();
        // 往下是对输入的参数合法性校验
        if (requestType == RequestType.HTTP) {
            return checkHttpRequest(session, dataContent);
        } else {
            return true;
        }
    }

    /**
     * 检查Http请求
     *
     * @param dataContent
     * @return
     */
    private boolean checkHttpRequest(WebSocketSession session, RequestDataDto dataContent) {
        boolean flag = false;
        // 把url的字符串进行截取，拼接host和port
        String url = dataContent.getUrl();
        String target = url.substring(0, 7);
        String targets = url.substring(0, 8);
        // 校验前7位
        if ("http://".equals(target)) {
            flag = true;
        } else if ("https://".equals(targets)) {
            flag = true;
        } else {
            String responseData = ResultEntity.failedWithMsg(OperationCommand.MISSING_PARAMETER, "请输入正确的Ip地址");
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
     * @param dataContent 数据
     */
    private void startTest(WebSocketSession session, RequestDataDto dataContent) {
        String host = "";
        int port = 0;
        if (requestType == RequestType.HTTP || requestType == RequestType.WebSocket) {
            try {
                URI url = new URI(dataContent.getUrl());
                host = url.getHost();
                port = url.getPort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestType == RequestType.UDP) {
            host = dataContent.getHost();
            port = dataContent.getPort();
            NettyClient client = new NettyClient(new NioDatagramChannel());
            this.nettyClient = client;
        } else if (requestType == RequestType.TCP) {
            host = dataContent.getHost();
            port = dataContent.getPort();
        }
        chooseInitializer(dataContent);
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        nettyClient.setAbstractNettyInitializer(abstractNettyInitializer);
        nettyClient.start(session, dataContent);
    }

    /**
     * 根据协议选择对应的处理器初始器 在nettyClient中还要通过chooseChannelHandlerContext()选择channelHandlerContext
     *
     * @param dataContent type--协议类型 ProtocolType--协议类型
     */
    public void chooseInitializer(RequestDataDto dataContent) {
        RequestType type = dataContent.getType();
        ProtocolType protocolType = dataContent.getProtocolType();
        if (type == RequestType.TCP) {
            if (protocolType == ProtocolType.modbus) {
                abstractNettyInitializer = new ModBusRequestInitializer();
            } else {
                abstractNettyInitializer = new TcpRequestInitializer();
            }
        } else if (type == RequestType.HTTP) {
            abstractNettyInitializer = new HttpRequestInitializer();
        } else if (type == RequestType.WebSocket) {
            abstractNettyInitializer = new WebSocketInitializer();
        } else if (type == RequestType.UDP) {
            abstractNettyInitializer = new UdpRequestInitializer();
        }
    }


}
