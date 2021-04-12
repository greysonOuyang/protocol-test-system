package com.yuyi.pts.websocket.origin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<WebSocketServer>();

    private Session session;

    private String sid = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketServerSet.add(this);
        addOnlineCount();
        log.debug("有新窗口开始监听：" + sid + "current online count :" + onlineCount);
        this.sid = sid;
        try {
            sendMessage("connect successful");
        } catch (IOException e) {
            log.debug("websockt io exception");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        webSocketServerSet.remove(this);
        decOnlineCount();
    }

    @OnMessage
    public void OnMessage(String message, Session session) {
        log.debug("receive message {} from {}", message, sid);
        for (WebSocketServer item: webSocketServerSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.debug("something occurs error");
        error.printStackTrace();
    }

    private static synchronized void decOnlineCount() {
        WebSocketServer.onlineCount.decrementAndGet();

    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    private static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount.get();
    }
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.incrementAndGet();
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketServerSet() {
        return webSocketServerSet;
    }

}
