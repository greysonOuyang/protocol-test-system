package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.cache.SessionWithChannelCache;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.client.handler.HttpRequestInitializer;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpRequestInitializer;
import com.yuyi.pts.netty.client.handler.WebSocketInitializer;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import com.yuyi.pts.service.ProtocolHandlerDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.nio.channels.SocketChannel;

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
    public void submitRequest(String host, Integer port, RequestType type) {
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        chooseInitializer(type);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start();
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
            nettyClientInitializer = new HttpRequestInitializer();
        } else if (type == RequestType.WebSocket) {
            nettyClientInitializer = new WebSocketInitializer();
        }
    }
}
