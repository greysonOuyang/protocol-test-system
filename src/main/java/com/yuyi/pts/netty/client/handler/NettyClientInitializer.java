package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 处理器初始化，有新的协议处理器加在此处
 *
 * @author greyson
 * @since 2021/4/11
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        // 协议分发处理器
//        pipeline.addLast(new InitialHandler());
//        pipeline.addLast(new WebSocketRequestHandler());
//        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
//        pipeline.addLast(new HttpServerCodec());
//        //支持写大数据流
//        pipeline.addLast(new ChunkedWriteHandler());
//        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new TcpRequestHandler());
    }
}
