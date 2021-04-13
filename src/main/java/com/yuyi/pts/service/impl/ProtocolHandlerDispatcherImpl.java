package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.cache.CtxWithSessionIdCache;
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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.nio.channels.SocketChannel;
import java.util.Map;

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
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        ChannelHandlerContext currentCtx = chooseInitializer(type);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        CtxWithSessionIdCache.put(session.getId(), currentCtx);
        nettyClient.start(host, port, currentCtx, dataContent);

//        RequestDataDto send = nettyClient.send(dataContent);
//        System.out.println(send);
    }




    /**
     * 根据协议选择对应的处理器初始器
     *
     * @param type 协议类型
     */
    public ChannelHandlerContext chooseInitializer(RequestType type) {
        ChannelHandlerContext currentCtx = null;
        if (type == RequestType.TCP) {
            nettyClientInitializer = new TcpRequestInitializer();
            currentCtx = TcpRequestHandler.myCtx;
        } else if (type == RequestType.HTTP) {
            nettyClientInitializer = new HttpRequestInitializer();
        } else if (type == RequestType.WebSocket) {
            nettyClientInitializer = new WebSocketInitializer();
        }
        return currentCtx;
    }
}
