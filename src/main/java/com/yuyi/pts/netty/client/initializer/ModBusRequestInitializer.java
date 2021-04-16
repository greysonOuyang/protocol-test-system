package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.handler.ModBusCodec;
import io.netty.channel.socket.SocketChannel;

/**
 * ModBus请求处理器初始化
 *
 * @author greyson
 * @since 2021/4/16
 */
public class ModBusRequestInitializer extends NettyClientInitializer{
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new ModBusCodec());
    }
}
