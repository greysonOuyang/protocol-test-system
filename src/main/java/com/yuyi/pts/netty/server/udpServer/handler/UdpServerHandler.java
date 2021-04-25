package com.yuyi.pts.netty.server.udpServer.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author : wzl
 * @date : 2021/4/25/9:31
 * @description:
 */

public class UdpServerHandler extends SimpleChannelInboundHandler<String> {

    // 需要注意的是这个对象要设置成为static 否则只能收到自己的消息，不能收到别人的消息

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 需要注意的是转发消息的时候要在后面加一个\n，因为我们在这个处理器的前面加了一个分隔符处理器DelimiterBasedFrameDecoder
        channelGroup.forEach(ch -> {
            if (ch == channel) {
                ch.writeAndFlush("[自己] " + msg + "\n");
            } else {
                ch.writeAndFlush("[" + channel.remoteAddress() + "]" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务器]- " + ctx.channel().remoteAddress() + " 加入\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[服务器]- " + ctx.channel().remoteAddress() + " 离开\n");
        // 这里不需要手动从channelGroup 中移除当前通道
        // 因为channelGroup 在初始化的时候使用的GlobalEventExecutor.INSTANCE会帮我们去干这个事情
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}