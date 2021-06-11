package com.yuyi.pts.controller;

import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.vo.request.ServerRequestDto;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.netty.client.initializer.TempClientInitializer;
import com.yuyi.pts.netty.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * description
 *
 * @author greyson
 * @since 2021/5/28
 */
@Controller
public class WebsocketServerController {

    public static NettyServer nettyServer;

    @Autowired
    InterfaceService interfaceService;

    @MessageMapping("/start/server")
    @SendTo("/topic/response")
    public void execute(ServerRequestDto request) {
        String serviceInterfaceId = request.getInterfaceId();
        TInterfaceConfig tInterfaceConfig = interfaceService.selectInterfaceById(serviceInterfaceId);
        String host = request.getHost();
        int port = request.getPort();
        String mode = request.getMode();
        if ("client".equals(mode)) {
            NettyClient nettyClient = new NettyClient();
            nettyClient.setHost(host);
            nettyClient.setPort(port);
            nettyClient.setServiceInterface(tInterfaceConfig);
            nettyClient.setNettyClientInitializer(new TempClientInitializer(tInterfaceConfig));
            nettyClient.start();
        } else if ("server".equals(mode)) {
            nettyServer = new NettyServer(tInterfaceConfig, port);
            nettyServer.start();
        }

    }

}
