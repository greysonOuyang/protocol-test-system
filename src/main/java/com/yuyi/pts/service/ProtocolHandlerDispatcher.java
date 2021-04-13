package com.yuyi.pts.service;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * 协议handler分发处理器
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
public interface ProtocolHandlerDispatcher {


    /**
     * 执行测试，提交请求；主要作用是根据host、port启动nettyClient，连接到第三方接口系统；根据请求协议选择不同的协议处理器；
     *
     * @param session
     * @param host
     * @param port
     * @param type
     * @param dataContent
     */
    void submitRequest(WebSocketSession session, String host, Integer port, RequestType type, RequestDataDto dataContent);
}
