package com.yuyi.pts.netty.client;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @Autowired
    private NettyClientInitializer nettyClientInitializer;

    @Autowired
    private TcpRequestHandler tcpRequestHandler;

    private final NioEventLoopGroup group;

    private final Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    /**
     * 客户端通道
     */
    private Channel clientChannel;

    public NettyClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
    }

    public void start(String host, int port, ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        try {
            if (nettyClientInitializer == null) {
                log.error("未能成功初始化NettyClientInitializer");
            } else {
                log.info("当前NettyClientInitializer类型为：" + nettyClientInitializer);
            }
            bootstrap.handler(nettyClientInitializer);
            channelFuture = bootstrap.connect(host, port).sync();

            //注册连接事件
            channelFuture.addListener((ChannelFutureListener)future -> {
                //如果连接成功
                if (future.isSuccess()) {
                    log.info("服务端[" + channelFuture.channel().localAddress().toString() + "]已连接...");
                    clientChannel = channelFuture.channel();
                }
                //如果连接失败，尝试重新连接
                else{
                    log.info("服务端[" + channelFuture.channel().localAddress().toString() + "]连接失败，重新连接中...");
                    future.channel().close();
                    bootstrap.connect(host, port);
                }
            });
            sendMessage(currentCtx, dataContent);
//            send(channelFuture, dataContent);
            channelFuture.channel().closeFuture().sync();

            //注册关闭事件
            channelFuture.channel().closeFuture().addListener(cfl -> {
                if(clientChannel!=null){
                    clientChannel.close();
                }
                log.info("客户端[" + channelFuture.channel().localAddress().toString() + "]已断开...");
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            log.info("channel关闭了");
        }
    }

    /**
     * 往第三方接口系统发送数据
     *
     * @param currentCtx
     * @param dataContent
     */
    private void sendMessage(ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        String result = JSON.toJSONString(dataContent);
        currentCtx.writeAndFlush(Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
    }



}
