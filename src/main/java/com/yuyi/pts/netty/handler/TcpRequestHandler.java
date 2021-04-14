package com.yuyi.pts.netty.handler;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.cache.CtxWithResponseMsgCache;
import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.service.ProcessResponseService;
import com.yuyi.pts.service.impl.ProcessResponseServiceImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
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
        super.channelActive(ctx);
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String content = in.toString(CharsetUtil.UTF_8);
        CtxWithResponseMsgCache.put(ctx, content);
        WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
        processResponseService.receiveDataAndSend2User(session, msg);
//        future = ctx.write("数据写入成功");
//        future.channel().closeFuture().awaitUninterruptibly();
//
//        TcpRequestHandler.future.addListener(ctl -> {
//            if (ctl.isSuccess()) {
//                CtxWithResponseMsgCache.isDataReady = true;
//            }
//        });
        log.info("CtxWithResponseMsgCache的放置结果：key--{}, value--{}", ctx.hashCode(), CtxWithResponseMsgCache.get(ctx));
//        if (CtxWithResponseMsgCache.get(ctx) != null) {
//            CtxWithResponseMsgCache.isDataReady = true;
//        }
    }

}
