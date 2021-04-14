package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.cache.CtxWithResponseMsgCache;
import com.yuyi.pts.common.cache.CtxWithSessionIdCache;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.client.handler.HttpRequestInitializer;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpRequestInitializer;
import com.yuyi.pts.netty.client.handler.WebSocketInitializer;
import com.yuyi.pts.service.ProtocolHandlerDispatcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

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
        chooseInitializer(type);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start(session, dataContent);
        receiveData(session);
    }

    public void receiveData(WebSocketSession session) {
        String id = session.getId();
        log.info("接收数据时的SessionId是：{}", id);
        // TODO 发送数据问题

        ChannelHandlerContext ctx = CtxWithSessionIdCache.get(id);
            try {
                Thread.sleep(1000);
                if (CtxWithResponseMsgCache.isDataReady) {
                    log.info("CtxWithSessionIdCache缓存的获取结果：key--{}, value--{}", id, ctx.hashCode());
                    Object responseData = CtxWithResponseMsgCache.get(ctx);
                    log.info("CtxWithResponseMsgCache缓存的获取结果：key--{}, value--{}", ctx.hashCode(), responseData);
                    String result = JSON.toJSONString(responseData);
                    synchronized (session) {
                        session.sendMessage(new TextMessage(result));
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
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
