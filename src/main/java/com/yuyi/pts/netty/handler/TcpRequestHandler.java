package com.yuyi.pts.netty.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

/**
 * TCP协议处理器
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public class TcpRequestHandler extends SimpleChannelInboundHandler<SocketChannel> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("欢迎来到客户端" + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {
        String content = JSON.toJSONString(msg);
        String data = "客户端的消息:" + content;
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8));
    }
}
