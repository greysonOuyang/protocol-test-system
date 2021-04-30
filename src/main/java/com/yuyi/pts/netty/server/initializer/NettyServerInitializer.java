package com.yuyi.pts.netty.server.initializer;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.netty.client.codec.ModBusCodecForServer;
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

    ServiceInterface serviceInterface = null;

    public NettyServerInitializer(ServiceInterface serviceInterface){
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

        pipeline.addLast(new ModBusCodecForServer());
        pipeline.addLast(new NettyServerHandler(serviceInterface));
    }
}
