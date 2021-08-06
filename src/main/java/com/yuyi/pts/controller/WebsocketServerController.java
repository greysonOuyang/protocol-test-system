package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.model.vo.request.RequestMainDTO;
import com.yuyi.pts.model.vo.request.RequestVo;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.NettyServer;
import com.yuyi.pts.netty.initializer.ProjectInitializer;
import com.yuyi.pts.service.ExecuteService;
import com.yuyi.pts.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * description
 *
 * @author greyson
 * @since 2021/5/28
 */
@Slf4j
@Controller
public class WebsocketServerController {

    public static NettyServer nettyServer;

    /**
     * 请求执行服务
     */
    @Autowired
    private ExecuteService executeService;

    @MessageMapping("/start/server")
    @SendTo("/topic/response")
    public void execute(RequestVo request) {
        String host = request.getHost();
        int port = request.getPort();
        String startMode = request.getStartMode();
        if (ConstantValue.CLIENT.equals(startMode)) {
            NettyClient nettyClient = new NettyClient();
            nettyClient.setHost(host);
            nettyClient.setPort(port);
            nettyClient.setAbstractNettyInitializer(new ProjectInitializer(request, nettyClient));
            nettyClient.start();
        } else if (ConstantValue.SERVER.equals(startMode)) {
            ProjectInitializer projectInitializer = new ProjectInitializer(request);
            nettyServer = new NettyServer(projectInitializer, port);
            nettyServer.start();
        }
    }

    @MessageMapping("/ws/ost")
    @SendTo("/topic/client/response")
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Object payload = message.getPayload();
        JSONObject jsonObject = JSON.parseObject((String) payload);
        RequestMainDTO requestMainDTO = JSON.toJavaObject(jsonObject, RequestMainDTO.class);
        log.info("receive client request：" + jsonObject.toString());
        int code = requestMainDTO.getCode();
        RequestDataDto data = requestMainDTO.getData();
        Object body = data.getBody();
        String content = JSON.toJSONString(body);
        if (log.isDebugEnabled()) {
            log.debug("receive client message:" + content);
        }
        if (code == OperationCommand.CANCEL.value()) {
            // 关闭连接
            session.close();
        } else if (code == OperationCommand.SUBMIT_TEST.value()) {
            executeService.execute(session, data);
        }
    }

}
