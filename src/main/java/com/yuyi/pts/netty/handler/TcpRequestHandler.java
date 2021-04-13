package com.yuyi.pts.netty.handler;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * TCP协议处理器
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
@Slf4j
public class TcpRequestHandler extends SimpleChannelInboundHandler<SocketChannel> {

    private ChannelHandlerContext ctx;
    private ChannelPromise promise;
    private RequestDataDto response;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
        ctx.write("欢迎来到客户端" + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {
        String content = JSON.toJSONString(msg);
        String data = "客户端的消息:" + content;
        System.out.println(data);
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8));
    }

    public synchronized ChannelPromise sendMessage(Object message) {
        while (ctx == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("等待ChannelHandlerContext实例化过程中出错",e);
            }
        }
        promise = ctx.newPromise();
        ctx.writeAndFlush(message);
        return promise;
    }

    public RequestDataDto getResponse(){
        return response;
    }
}
