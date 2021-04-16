package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.handler.HttpRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/13
 */
public class HttpRequestInitializer extends NettyClientInitializer {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new IdleStateHandler(3, 0, 0),
                        // new HttpRequestDecoder(),new HttpResponseEncoder() 两个类的组合
                        new HttpClientCodec(),
                        //针对POST方式请求，信息是保存在message body中的,如果只是单纯的用HttpServerCodec是无法完全的解析Http POST请求的，因为HttpServerCodec只能获取uri中参数，所以需要加上HttpObjectAggregator
                        new HttpObjectAggregator(65536),
                        //自定义的
                        new HttpRequestHandler());
    }
}
