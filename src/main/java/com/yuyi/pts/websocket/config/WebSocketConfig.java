package com.yuyi.pts.websocket.springboot.config;

import com.yuyi.pts.websocket.springboot.handler.HttpAuthHandler;
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
    private HttpAuthHandler httpAuthHandler;
//    @Autowired
//    private MyInterceptor myInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(httpAuthHandler, "ws/serverOne")
//                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*");
    }
}