package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.client.handler.UdpRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author : wzl
 * @date : 2021/4/20/9:21
 * @description:
 */
public class UdpRequestInitializer extends NettyClientInitializer {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new IdleStateHandler(3, 0, 0),
                        // new HttpRequestDecoder(),new HttpResponseEncoder() 两个类的组合
                        new HttpClientCodec(),
                        //自定义的
                        new UdpRequestHandler());
    }
}
