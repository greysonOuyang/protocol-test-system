package com.yuyi.pts.netty.server;

import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.netty.server.initializer.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * 模拟信号发送给客户端
 *
 * @author  wzl
 * @date  2021/4/27/9:07
 */
@Slf4j
public class NettyServer {
    ServerBootstrap serverBootstrap =  new ServerBootstrap();
    EventLoopGroup boss =null;
    EventLoopGroup worker =null;
    ChannelFuture future = null;

    ServiceInterface serviceInterface;

    int port;

    public NettyServer(ServiceInterface service, int port){
        this.serviceInterface = service;
        this.port = port;
    }

    @PreDestroy
    public void stop(){
        if(future!=null){
            future.channel().close().addListener(ChannelFutureListener.CLOSE);
            future.awaitUninterruptibly();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            future=null;
            log.info(" 服务关闭 ");
        }
    }
    public void start(){
        log.info(" nettyServer 正在启动");

        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        log.info("netty服务器在["+this.port+"]端口启动监听");

        serverBootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new NettyServerInitializer(serviceInterface));


        try{
            future = serverBootstrap.bind(this.port).sync();
            if(future.isSuccess()){
                log.info("nettyServer 完成启动 ");
            }
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.info("nettyServer 启动时发生异常---------------{}", e.getMessage());
            log.info(e.getMessage());
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
