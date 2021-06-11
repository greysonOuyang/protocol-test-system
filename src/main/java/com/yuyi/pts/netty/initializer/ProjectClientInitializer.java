package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.netty.handler.ProjectConfigHandler;
import com.yuyi.pts.netty.codec.ModBusDecoder;
import com.yuyi.pts.netty.codec.SmartCarEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 根据项目配置处理信息的处理器
 *
 * @author greyson
 * @since 2021/6/11
 */
public class ProjectClientInitializer extends AbstractNettyInitializer<SocketChannel> {
    TInterfaceConfig serviceInterface = null;

    public ProjectClientInitializer(TInterfaceConfig serviceInterface){
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
        pipeline.addLast(new ProjectConfigHandler(serviceInterface, Constant.CLIENT));
    }
}
