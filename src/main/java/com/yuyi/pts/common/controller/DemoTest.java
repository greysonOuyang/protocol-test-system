package com.yuyi.pts.common.controller;/*
 * @author : wzl
 * @date   : 2021/4/12/14:23
 * @description:
 */

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.vo.request.DataContentDTO;
import com.yuyi.pts.netty.NettyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前后端交互的类实现消息的接收推送(自己发送给自己)
 *
 * @ServerEndpoint(value = "/test/one") 前端通过此URI和后端交互，建立连接
 */

@ServerEndpoint(value = "/test/one")
@Component
@Slf4j
public class DemoTest {
    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    NettyClient nettyClient = new NettyClient();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen( Session session) {

        onlineCount.incrementAndGet(); // 在线数加1
        System.out.println("有新连接加入：{}，当前在线人数为：{}" + session.getId() + onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        System.out.println("有一连接关闭：{}，当前在线人数为：{}" + session.getId() + onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject object = JSONObject.parseObject(message);
        DataContentDTO dcd = new DataContentDTO();
        DataContentDTO.RequestDataDTO requestData = dcd.getRequestData();
        if(null!=object){
            // 获取到前端上送的ip、端口、类型、具体内容
            System.out.println(",,,,"+object.get("ip").toString());
         //   requestData.setHost(object.get("ip").toString());
        }
        // 将IP 端口获取到
        nettyClient.setHost(object.get("ip").toString());
        nettyClient.setPort(Integer.parseInt(object.get("port").toString()));
        // 在这启动netty客户端，调用第三方接口服务
        nettyClient.start();

        System.out.println("服务端收到客户端[{}]的消息:{}" + session.getId() + message);
        //  将信息传给前端
        this.sendMessage("Hello,ip为" + object.get("ip")+"端口为："+object.get("port"),session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            System.out.println("服务端给客户端[{}]发送消息{}" + toSession.getId() + message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            System.out.println("服务端发送消息给客户端失败：{}" + e);
        }
    }


}
