package com.yuyi.pts.service;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.vo.request.RequestDataDto;
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
     * @param host 测试系统ip
     * @param port 测试系统端口
     * @param type 请求类型
     * @param dataContent 请求数据
     */
    void submitRequest(WebSocketSession session, String host, Integer port, RequestType type, RequestDataDto dataContent);

}
