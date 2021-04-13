package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/13
 */
@Service
public class TcpRequestInitializer extends NettyClientInitializer {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TcpRequestHandler());
    }
}
