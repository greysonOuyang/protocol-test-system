package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.cache.ObjCache;
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

import java.net.URI;
import java.net.URL;

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


    @Override
    public void submitHttpRequest(WebSocketSession session,  RequestType type, RequestDataDto dataContent) {

        // 把url的字符串进行截取，拼接host和port
        String url = dataContent.getUrl();
        String flag = url.substring(5,5);
        //http(s)://127.0.0.1/8080/test  按这个规则进行截取
         if("s".equals(flag)){
             String host = url.substring(8,17);
             nettyClient.setHost(host);
             String port = url.substring(18,22);
             nettyClient.setPort(Integer.parseInt(port));
             String uri = url.substring(22);
             ObjCache.put("uri",uri);
         }else {
             String host = url.substring(7,16);
             nettyClient.setHost(host);
             String port = url.substring(17,21);
             nettyClient.setPort(Integer.parseInt(port));
             String uri = url.substring(21);
             ObjCache.put("uri",uri);
         }
        chooseInitializer(type);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start(session, dataContent);
        System.out.println("test---------");
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
