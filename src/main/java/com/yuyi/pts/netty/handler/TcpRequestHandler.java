package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.response.ResponseInfo;
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
 * TCP协议处理器、需要将ChannelHandlerContext作为全局属性且是静态；
 * 其他协议的自定义Handler类同，辅助NettyClient在发送消息时获取到ctx
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
@Slf4j
public class TcpRequestHandler extends ChannelInboundHandlerAdapter {

    public static ChannelHandlerContext myCtx;

    public static ResponseService responseService;

    static {
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("Tcp处理器已经被添加");
        myCtx = ctx;
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端发回的数据：{}", msg);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setState(1);
        responseInfo.setBody((String) msg);
        String result = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
        responseService.sendTextMsg(ctx, result);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Tcphandler出现错误：{}", cause.getMessage());
    }
}
