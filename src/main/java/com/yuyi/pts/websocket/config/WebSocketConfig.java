package com.yuyi.pts.websocket.config;

import com.yuyi.pts.websocket.handler.HandlerDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Autowired
    private HandlerDispatcher handlerDispatcher;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handlerDispatcher, "ws/ost")

                .setAllowedOrigins("*");
    }
}