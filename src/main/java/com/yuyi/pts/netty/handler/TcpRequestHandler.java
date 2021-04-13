package com.yuyi.pts.netty.handler;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

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
        String content = JSON.toJSONString(msg);
        log.info("收到服务端返回的消息：{}", content);
    }

}
