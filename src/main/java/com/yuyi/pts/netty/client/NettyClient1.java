package com.yuyi.pts.netty.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.vo.request.ConfigRequest;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.netty.client.handler.NettyClientHandler;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * NettyClient 通过指定IP、PORT连接接口系统进行数据请求
 *
 * @author wzl
 * @since 2021/4/11
 */
@Component
@Slf4j
@Data
public class NettyClient1 {

    @Autowired
    private ProtocolConfig protocolConfig;

    Channel channel = null;

    Bootstrap bootstrap1 = new Bootstrap();

    @Value("${user.protocol.ip}")
    private String host;

    @Value("${user.protocol.port}")
    private int port;

    Bootstrap bootstrap = new Bootstrap();

    private NettyClientHandler nettyClientHandler;

    private ChannelFuture channelFuture;


    public NettyClient1() {

    }

    public void start(String type, RequestDataDto data) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap(); // (1)
        // 根据不同的类型去匹配对应的协议
        if(("TCP").equals(type)){
            bootstrap.group(workerGroup)
                     .option(ChannelOption.SO_KEEPALIVE, true)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                     ch.pipeline().addLast(new IdleStateHandler(1, 1, 0)
                    ,new TcpHandler());
                }
            });
            doTcpConnect(type,data);
        } else if (("http").equals(type)){
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(
                            new IdleStateHandler(1, 1, 0)

                    );
                }
            });
            doHttpConnect();
        }else if (("websocket").equals(type)){
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(
                            new IdleStateHandler(1, 1, 0)

                    );
                }
            });
            doWebsocketConnect();
        }

    }
    // websocket连接
    private void doWebsocketConnect() {
    }
    // http连接
    private void doHttpConnect() {
    }

    int currentPort = 1; // 当前的端口
    public void doTcpConnect(String type,RequestDataDto data) {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect(data.getHost(), data.getPort());// getCurrentPort() 获取到 IP 这是已经初始化后的port
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(data), Charset.defaultCharset());
                log.info("连接成功-----");
                ChannelFuture channelFuture = future.channel().writeAndFlush(byteBuf);
                channel = futureListener.channel();
            } else {
                currentPort = currentPort == 1 ? 2 : 1;
                log.info("连接服务器[" + getHost() + ":" + getPort() + "]失败,10s后重试");
            }
        });
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
