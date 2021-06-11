package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.netty.handler.UdpRequestHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author : wzl
 * @date : 2021/4/20/9:21
 * @description:
 */
public class UdpRequestInitializer extends AbstractNettyInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()),
                        new StringEncoder(CharsetUtil.UTF_8),
                        new StringDecoder(CharsetUtil.UTF_8),
                        //自定义的
                        new UdpRequestHandler());
    }

}
