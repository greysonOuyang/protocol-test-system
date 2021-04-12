package com.yuyi.pts.netty;

import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.netty.handler.NettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * NettyClient 通过指定IP、PORT连接接口系统进行数据请求
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public class NettyClient {

    @Autowired
    private ProtocolConfig protocolConfig;

    @Value("${user.protocol.ip}")
    private String host;

    @Value("${user.protocol.port}")
    private int port;

    private final NioEventLoopGroup group;

    private final Bootstrap bootstrap;

    private ChannelFuture channelFuture;

//    private static class SingletonNettyClient{
//        static final NettyClient INSTANCE = new NettyClient();
//    }
//
//    public static NettyClient getInstance(){
//
//        return SingletonNettyClient.INSTANCE;
//    }



    public NettyClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new NettyClientInitializer());
    }

    public void start() {
        try {
            channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private int getPort() {
        return 0;
    }

    private String getHost() {
        return null;
    }

}
