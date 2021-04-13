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


    void submitRequest(WebSocketSession session, String host, Integer port, RequestType type, RequestDataDto dataContent);
}
