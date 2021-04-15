package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.cache.CtxWithResponseMsgCache;
import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.cache.ObjCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ApplicationHelper;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.service.ProcessResponseService;
import com.yuyi.pts.service.impl.ProcessResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;

/**
 * HTTP(S)协议处理器、需要将ChannelHandlerContext作为全局属性且是静态；
 * 其他协议的自定义Handler类同，辅助NettyClient在发送消息时获取到ctx
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
@Slf4j
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
    private static ProcessResponseService processResponseService;

    static {
        processResponseService = SpringUtils.getBean(ProcessResponseServiceImpl.class);
    }
    public static ChannelHandlerContext myCtx;
    public static ChannelFuture future;
    private ChannelPromise promise;
    private RequestDataDto response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myCtx = ctx;
        Object url = ObjCache.get("uri");
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, url.toString());
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        ctx.writeAndFlush(request);
        super.channelActive(ctx);
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("msg -> "+msg);
        if(msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse)msg;
            ByteBuf buf = response.content();
            String result = buf.toString(CharsetUtil.UTF_8);
            String responseData = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE.value(), result);
            WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
            session.sendMessage(new TextMessage(responseData));
            System.out.println("response -> "+result);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
