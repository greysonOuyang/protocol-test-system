package com.yuyi.pts.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Server handler
 *
 * @author greyson
 * @since 2021/4/11
 */
@Slf4j
@ChannelHandler.Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("欢迎来到" + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("消息处理完毕", CharsetUtil.UTF_8));
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);

        ByteBuf buf = (ByteBuf) msg;
        String result1 = buf.toString(CharsetUtil.UTF_8);
        String content = JSON.toJSONString(msg);
        HashMap<String, String> map = new HashMap<>();
        map.put("server", ctx.channel().remoteAddress().toString());
        map.put("responseData", "我是服务器返回的数据");
        String result = JSON.toJSONString(map);
        log.debug("服务端{}收到客户端消息：{}", ctx.channel().remoteAddress(), result1);

        byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        log.info("服务端往客户端发送的数据：" + result);
        ctx.writeAndFlush(buffer);
    }

}
