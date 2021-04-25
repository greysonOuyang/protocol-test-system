package com.yuyi.pts.netty.server.udpServer;

import com.yuyi.pts.netty.server.udpServer.handler.UdpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzl
 * @date : 2021/4/25/9:30
 * @description:
 */
@Component
@Slf4j
public class UdpServer {


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    log.info("有客户端连接到服务器:{}", ch.remoteAddress());
                    ChannelPipeline pipeline = ch.pipeline();
                    // 这个解码器的作用可以按照指定的分隔符进行分包
                    // Delimiters.lineDelimiter() 换行分隔符 \r\n
                    pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    // 注入自定义的处理器
                    pipeline.addLast(new UdpServerHandler());

                }
            });
            ChannelFuture channelFuture = bootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}


