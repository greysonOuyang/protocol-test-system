package com.yuyi.pts.testServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 *  测试NettyClient发送数据
 *
 * @author greyson
 * @since 2021/4/11
 */
public class MyServer {


    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                              socketChannel.pipeline()
                               .addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
                                .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
                               .addLast(new LoggingHandler(LogLevel.DEBUG))

//                                      .addLast(new JsonObjectDecoder())
//                                      .addLast(new StringDecoder(Charset.forName("utf-8")))
                                      .addLast(new MyServerHandler());

                            }
                        }

                    );
            System.out.println("服务端已经准备就绪...");
            ChannelFuture channelFuture = serverBootstrap.bind(2100).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
