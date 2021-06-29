package com.yuyi.pts.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/28
 */
@Slf4j
public class AtsReceiveHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("添加了接收ATS信息的处理器");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;
        byte[] bytes = new byte[msg1.readableBytes()];
        msg1.readBytes(bytes);
        log.info("收到了转发来的ATS信息：[{}]", bytes);
    }
}
