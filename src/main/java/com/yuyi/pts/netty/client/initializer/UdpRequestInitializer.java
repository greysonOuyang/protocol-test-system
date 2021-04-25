package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.client.handler.UdpRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

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
                        new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()),
                        new StringEncoder(CharsetUtil.UTF_8),
                        new StringDecoder(CharsetUtil.UTF_8),
                        //自定义的
                        new UdpRequestHandler());
    }
}
