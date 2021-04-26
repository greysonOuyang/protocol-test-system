package com.yuyi.pts.testServer.udpServer.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzl
 * @date : 2021/4/25/9:31
 * @description:
 */
@Slf4j
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    //在事件循环中执行
    private EventExecutor executor;

    //客户端与服务器端连接成功的时候触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("UDP通道已经连接");
        System.out.println("UDP通道已经连接");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
            throws Exception {
        // 读取收到的数据
        ByteBuf buf = (ByteBuf) packet.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, CharsetUtil.UTF_8);
        System.out.println("【NOTE】>>>>>> 收到客户端的数据：" + body);

        // 回复一条信息给客户端
        ctx.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer("fff"
                        , CharsetUtil.UTF_8)
                , packet.sender()));
    }

    //消息没有结束的时候触发
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //logger.log(Level.INFO, "AuthServerInitHandler exceptionCaught");
        log.error("UdpServerHandler exceptionCaught" + cause.getMessage());
        System.out.println("UdpServerHandler exceptionCaught" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
