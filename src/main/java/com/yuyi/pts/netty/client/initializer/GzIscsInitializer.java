package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.client.codec.AtsCodec;
import com.yuyi.pts.netty.client.handler.GzIscsRequestHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * 广州综合监控
 *
 * @author greyson
 * @since 2021/4/25
 */
public class GzIscsInitializer extends NettyClientInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new AtsCodec(),
                        new GzIscsRequestHandler()
                );
    }
}
