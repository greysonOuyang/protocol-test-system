package com.yuyi.pts.netty.server.initializer;

import com.yuyi.pts.netty.client.handler.TcpRequestHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * @author : wzl
 * @date : 2021/4/27/9:31
 * @description:
 */
public class TcpServerInitializer extends NettyServerInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TcpRequestHandler());
    }
}
