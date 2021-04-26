package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.ProtocolType;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.config.NettyClientConfig;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.client.initializer.*;
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
    private NettyClientConfig nettyClientConfig;

    @Autowired
    private NettyClientInitializer nettyClientInitializer;


    @Override
    public void submitTCPRequest(WebSocketSession session, RequestDataDto dataContent) {
        chooseInitializer(dataContent);
        nettyClient.setHost(dataContent.getHost());
        nettyClient.setPort(dataContent.getPort());
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start(dataContent.getType(),session, dataContent);
    }


    @Override
    public void submitHttpRequest(WebSocketSession session, RequestDataDto dataContent) {

        // 把url的字符串进行截取，拼接host和port
        String url = dataContent.getUrl();
        String flag = url.substring(5,5);
        //http(s)://127.0.0.1/8080/test  按这个规则进行截取
         if("s".equals(flag)){
             String host = url.substring(8,17);
             nettyClient.setHost(host);
             String port = url.substring(18,22);
             nettyClient.setPort(Integer.parseInt(port));
         }else {
             String host = url.substring(7,16);
             nettyClient.setHost(host);
             String port = url.substring(17,21);
             nettyClient.setPort(Integer.parseInt(port));
         }
        chooseInitializer(dataContent);
        nettyClient.setNettyClientInitializer(nettyClientInitializer);
        nettyClient.start(dataContent.getType(),session, dataContent);
        System.out.println("test---------");
    }

    @Override
    public void submitUdpRequest(WebSocketSession session, RequestDataDto dataContent) {
//        NettyClient client = new NettyClient(dataContent.getHost());
        NettyClient client = NettyClientConfig.getNettyUdp();
        chooseInitializer(dataContent);
        client.setHost(dataContent.getHost());
        client.setPort(dataContent.getPort());
        client.setNettyClientInitializer(nettyClientInitializer);
        client.start(dataContent.getType(),session, dataContent);
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
                nettyClientInitializer = new ModBusRequestInitializer();
            } else if (protocolType == ProtocolType.none) {
                nettyClientInitializer = new TcpRequestInitializer();
            } else if (protocolType == ProtocolType.gzIscs) {
                nettyClientInitializer = new GzIscsInitializer();
            } else {
                nettyClientInitializer = new TcpRequestInitializer();
            }
        } else if (type == RequestType.HTTP) {
            nettyClientInitializer = new HttpRequestInitializer();
        } else if (type == RequestType.WebSocket) {
            nettyClientInitializer = new WebSocketInitializer();
        } else if (type == RequestType.UDP) {
            nettyClientInitializer = new UdpRequestInitializer();
        }
    }
}
