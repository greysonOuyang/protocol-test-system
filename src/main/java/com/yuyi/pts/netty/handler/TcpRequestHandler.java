package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.cache.CtxWithRequestDataCCache;
import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.service.ProcessRequestService;
import com.yuyi.pts.service.ProcessResponseService;
import com.yuyi.pts.service.impl.ProcessResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
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

    private static ProcessResponseService processResponseService;

    private static ProcessRequestService processRequestService;


    static {
        processResponseService = SpringUtils.getBean(ProcessResponseServiceImpl.class);
        processRequestService = SpringUtils.getBean(ProcessRequestService.class);
    }

    public static ChannelHandlerContext myCtx;



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        myCtx = ctx;
        RequestDataDto requestDataDto = CtxWithRequestDataCCache.get(ctx);
        // 连接上服务器之后则发送消息
        processRequestService.sendBinMessage(ctx, requestDataDto);
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String content = in.toString(CharsetUtil.UTF_8);
        WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
        processResponseService.receiveDataAndSend2User(session, content);

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
