package com.yuyi.pts.netty.server.initializer;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.netty.codec.ModBusDecoder;
import com.yuyi.pts.netty.codec.SmartCarEncoder;
import com.yuyi.pts.netty.server.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * NettyServerInitializer
 *
 * @author greyson
 * @since 2021/4/27
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    TInterfaceConfig serviceInterface = null;

    public NettyServerInitializer(TInterfaceConfig serviceInterface){
        this.serviceInterface = serviceInterface;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(
                Constant.SERVER_READ_IDLE_TIME_OUT,
                Constant.SERVER_WRITE_IDLE_TIME_OUT,
                Constant.SERVER_ALL_IDLE_TIME_OUT,
                TimeUnit.SECONDS));

        pipeline.addLast(new SmartCarEncoder());
        pipeline.addLast(new ModBusDecoder());
        pipeline.addLast(new NettyServerHandler(serviceInterface));
    }
}
