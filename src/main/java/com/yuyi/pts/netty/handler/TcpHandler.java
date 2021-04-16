package com.yuyi.pts.netty.handler;
/**
 * @author : wzl
 * @date   : 2021/4/12/17:23
 * @description:
 */

import com.yuyi.pts.common.util.ApplicationHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TcpHandler  extends SimpleChannelInboundHandler<ByteBuf> {

    private RedisTemplate redisTemplate = ApplicationHelper.getBean("redisTemplate");

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("wwwwzzzzz  rocks!", CharsetUtil.UTF_8));
    }

    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        ByteBuf in = msg;
        String msg1 = in.toString(CharsetUtil.UTF_8);
        redisTemplate.boundValueOps("StringKey").set(msg1);
        System.out.println("redis的缓存值"+redisTemplate.boundValueOps("StringKey").get());
        System.out.println("读取服务端channelRead0="+in.toString(CharsetUtil.UTF_8));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
