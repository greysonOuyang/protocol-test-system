package com.yuyi.pts.service;

import com.yuyi.pts.model.vo.request.RequestDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
public interface ExecuteService {

    /**
     * 执行提交测试任务
     *
     * @param session 会话
     * @param dataContent 待发送的数据
     */
    void execute(WebSocketSession session, RequestDataDto dataContent) throws IOException;
}
