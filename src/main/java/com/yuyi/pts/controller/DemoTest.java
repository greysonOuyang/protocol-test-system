package com.yuyi.pts.controller;

/**
 * 前后端交互的类实现消息的接收推送(自己发送给自己)
 *
 * @ServerEndpoint(value = "/test/one") 前端通过此URI和后端交互，建立连接
 **/

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ApplicationHelper;
import com.yuyi.pts.netty.client.NettyClient1;
import com.yuyi.pts.service.impl.NettyMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint(value = "/test/one")
@Slf4j
public class DemoTest {
    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private RedisTemplate redisTemplate = ApplicationHelper.getBean("redisTemplate");


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
        NettyMessageServiceImpl nettyMessageServiceImpl = new NettyMessageServiceImpl();
        JSONObject object1 = nettyMessageServiceImpl.getConnect(message);
        System.out.println("服务端收到客户端[{}]的消息:{}" + session.getId() + message);
        //  将信息传给前端
        this.sendMessage("Hello,ip为" + object1.get("ip")+"端口为："+object1.get("port"),session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message, Session session) {
        try {
            Object o = redisTemplate.boundValueOps("StringKey").get();
            System.out.println("服务端发送消息给客户端成功"+ o);
            session.getBasicRemote().sendText(o.toString());
        } catch (Exception e) {
            System.out.println("服务端发送消息给客户端失败：{}" + e);
        }
    }
}
