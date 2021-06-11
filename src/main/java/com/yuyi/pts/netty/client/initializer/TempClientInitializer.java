package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.netty.client.handler.TempClientHandler;
import com.yuyi.pts.netty.codec.ModBusDecoder;
import com.yuyi.pts.netty.codec.SmartCarEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/11
 */
public class TempClientInitializer extends NettyClientInitializer<SocketChannel> {
    TInterfaceConfig serviceInterface = null;

    public TempClientInitializer(TInterfaceConfig serviceInterface){
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
//        pipeline.addLast(new SmartCarDecoder14());
//        pipeline.addLast(new SmartCarEncoder14());
        pipeline.addLast(new SmartCarEncoder());
        pipeline.addLast(new ModBusDecoder());
        pipeline.addLast(new TempClientHandler(serviceInterface));
    }
}