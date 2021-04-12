package com.yuyi.pts.netty.client;

import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * NettyClient 通过指定IP、PORT连接接口系统进行数据请求
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
@Slf4j
@Data
public class NettyClient {

    @Autowired
    private ProtocolConfig protocolConfig;

    Channel channel = null;

    Bootstrap bootstrap1 = new Bootstrap();

    @Value("${user.protocol.ip}")
    private String host;

    @Value("${user.protocol.port}")
    private int port;

    private final NioEventLoopGroup group;

    private final Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    public NettyClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new NettyClientInitializer());
    }

    public void start() {
        try {
            channelFuture = bootstrap.connect(getHost(), getPort()).sync();
            log.info("连接成功-------");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
