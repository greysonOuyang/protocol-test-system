package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * 广州综合监控协议请求处理
 *
 * @author greyson
 * @since 2021/4/25
 */
@Slf4j
public class GzIscsRequestHandler extends ChannelInboundHandlerAdapter {

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


    // TODO iscs读数据处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
