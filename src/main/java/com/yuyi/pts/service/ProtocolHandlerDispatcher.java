package com.yuyi.pts.service;

import com.yuyi.pts.model.vo.request.RequestDataDto;
import org.springframework.web.socket.WebSocketSession;

/**
 * 协议handler分发处理器
 *
 * @author greyson
 * @since 2021/4/12
 */
public interface ProtocolHandlerDispatcher {


    /**
     * 执行TCP测试，提交请求；主要作用是根据host、port启动nettyClient，连接到第三方接口系统；根据请求协议选择不同的协议处理器；
     *
     * @param session websocket会话
     * @param dataContent 请求数据
     */
    void submitTCPRequest(WebSocketSession session, RequestDataDto dataContent);


    /**
     * 执行HTTP测试，提交请求；主要作用是根据host、port启动nettyClient，连接到第三方接口系统；根据请求协议选择不同的协议处理器；
     *
     * @param session websocket会话
     * @param dataContent 请求数据
     */
    void submitHttpRequest(WebSocketSession session, RequestDataDto dataContent);

    void submitUdpRequest(WebSocketSession session,  RequestDataDto dataContent);

}
