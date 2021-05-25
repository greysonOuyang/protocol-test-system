package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.client.handler.WebSocketRequestHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/13
 */
public class WebSocketInitializer extends NettyClientInitializer<SocketChannel>{
    @Override
    protected void  initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpClientCodec(),
                new HttpObjectAggregator(1024*1024*10));
        p.addLast("hookedHandler", new WebSocketRequestHandler());
    }
}
