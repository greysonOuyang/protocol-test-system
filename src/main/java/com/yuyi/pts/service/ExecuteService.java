package com.yuyi.pts.service;

import com.yuyi.pts.common.vo.request.RequestDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
public interface ExecuteService {

    public void execute(WebSocketSession session, RequestDataDto dataContent);
}
