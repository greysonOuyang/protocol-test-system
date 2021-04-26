package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.ModBusMessage;
import com.yuyi.pts.model.vo.response.ResponseInfo;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author : wzl
 * @date : 2021/4/16/14:21
 * @description: Modbus协议处理器
 */
@Component
@Slf4j
public class ModbusRequestHandler extends ChannelInboundHandlerAdapter {

    public static ChannelHandlerContext myCtx;

    public static ResponseService responseService;

    static {
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
            log.debug("已经 3s 没有读到数据了");
            WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
            session.close();
            ctx.channel().close();
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("ModbusRequestHandler处理器已经被添加");
        myCtx = ctx;
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("modbus协议连接已建立");
    }

    /**
     * 获取到接口系统返回的数据，将其返回至前端
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端返回的数据：{}", msg);
        // 处理数据返回给前端
        ModBusMessage result = (ModBusMessage) msg;
        String codeHexStr = result.getCode();
        int code = Integer.parseInt(codeHexStr);
        String body = result.getBody();
        String returnMsg = "";
        // TODO 验证大小端模式
        if ("8004".equals(codeHexStr) || "8010".equals(codeHexStr)) {
            log.info("服务端返回了异常消息...");
            if ("00".equals(body)) {
                returnMsg = "请求超时";
            } else if ("01".equals(body)) {
                returnMsg = "接收到的请求中的功能码是主机或从机不允许执行的";
            } else if ("02".equals(body)) {
                returnMsg = "收到的数据请求中的数据地址是从机或者主机不允许的值";
            } else if ("03".equals(body)) {
                returnMsg = "请求数据字段的值是从机或者主机中不允许的值";
            } else if ("04".equals(body)) {
                returnMsg = "从机或者主机试图执行所要求的操作时产生了不可恢复的错误";
            }
        }
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(code);
        responseInfo.setMsg(returnMsg);
        responseInfo.setBody(body);
        responseInfo.setState(ResponseInfo.SUCCESS);
        String response = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE,  responseInfo);
        responseService.sendTextMsg(ctx, response);
    }


//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("ModbusRequestHandler出现错误：{}", cause.getMessage());
//    }


}
