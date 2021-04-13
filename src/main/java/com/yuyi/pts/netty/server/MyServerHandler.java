package com.yuyi.pts.netty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;
import java.util.Locale;

/**
 * Server handler
 *
 * @author greyson
 * @since 2021/4/11
 */
@Slf4j
@ChannelHandler.Sharable
public class MyServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("欢迎来到" + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息", CharsetUtil.UTF_8));
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("服务端{}收到客户端消息：{}", ctx.channel().remoteAddress(), msg);
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息" + msg, CharsetUtil.UTF_8));
    }
}
