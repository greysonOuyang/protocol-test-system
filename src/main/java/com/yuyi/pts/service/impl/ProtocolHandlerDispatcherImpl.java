package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.client.handler.HttpRequestInitializer;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpRequestInitializer;
import com.yuyi.pts.netty.client.handler.WebSocketInitializer;
import com.yuyi.pts.service.ProtocolHandlerDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
@Slf4j
public class ProtocolHandlerDispatcherImpl implements ProtocolHandlerDispatcher {

    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private NettyClientInitializer nettyClientInitializer;


    @Override
    public void submitRequest(WebSocketSession session, String host, Integer port, RequestType type, RequestDataDto dataContent) {
        // TODO 此处可根据不同类型进行任务分发
        chooseInitializer(type);
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start(session, dataContent);
    }


    /**
     * 根据协议选择对应的处理器初始器
     *
     * @param type 协议类型
     */
    public void chooseInitializer(RequestType type) {
        if (type == RequestType.TCP) {
            nettyClientInitializer = new TcpRequestInitializer();
        } else if (type == RequestType.HTTP) {
            // TODO 同上 指定currentCtx
            nettyClientInitializer = new HttpRequestInitializer();
        } else if (type == RequestType.WebSocket) {
            // TODO 同上 指定currentCtx
            nettyClientInitializer = new WebSocketInitializer();
        }
    }
}
