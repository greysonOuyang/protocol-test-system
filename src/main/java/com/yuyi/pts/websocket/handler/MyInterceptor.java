//package com.yuyi.pts.websocket.handler;
//
//import io.netty.handler.codec.http.HttpUtil;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 前置拦截器、进行请求校验
// *
// * @author greyson
// * @since 2021/4/12
// */
//@Component
//public class MyInterceptor implements HandshakeInterceptor {
//
//    /**
//     * 握手前
//     *
//     * @param request
//     * @param response
//     * @param wsHandler
//     * @param attributes
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        System.out.println("握手开始");
//        // 获得请求参数
//        HashMap<String, String> paramMap = decodeParamMap(request.getURI().getQuery(), "utf-8");
//        String uid = paramMap.get("token");
//        if (!StringUtils.isEmpty(uid)) {
//            // 放入属性域
//            attributes.put("token", uid);
//            System.out.println("用户 token " + uid + " 握手成功！");
//            return true;
//        }
//        System.out.println("用户登录已失效");
//        return false;
//    }
//
//    private HashMap<String, String> decodeParamMap(String query, String encode) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("token", query);
//        return map;
//    }
//
//    /**
//     * 握手后
//     *
//     * @param request
//     * @param response
//     * @param wsHandler
//     * @param exception
//     */
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
//        System.out.println("握手完成");
//    }
//
//}