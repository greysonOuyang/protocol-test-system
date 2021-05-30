package com.yuyi.pts.controller;

import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ServerRequestDto;
import com.yuyi.pts.netty.server.NettyServer;
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

    NettyServer nettyServer;

    @MessageMapping("/start/server")
    @SendTo("/topic/response")
    public String execute( ServerRequestDto request) {
        String serviceInterfaceId = request.getInterfaceId();
        ServiceInterface serviceInterface = InterfaceCache.get(serviceInterfaceId);
        int port = request.getPort();
        nettyServer = new NettyServer(serviceInterface, port);
        nettyServer.start();
//        NettyServerHandler.RETURN_MAP.forEach(item -> {
//
//        });
        return "success";
    }
}
