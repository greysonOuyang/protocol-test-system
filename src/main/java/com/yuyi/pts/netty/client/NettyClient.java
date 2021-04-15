package com.yuyi.pts.netty.client;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.netty.client.handler.HttpRequestInitializer;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpRequestInitializer;
import com.yuyi.pts.netty.client.handler.WebSocketInitializer;
import com.yuyi.pts.netty.handler.HttpRequestHandler;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    private ChannelHandlerContext currentCtx;

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

    public void start(WebSocketSession session, RequestDataDto dataContent) {
        try {
            if (nettyClientInitializer == null) {
                log.error("未能成功初始化NettyClientInitializer");
            } else {
                log.info("当前NettyClientInitializer类型为：" + nettyClientInitializer);
            }
            bootstrap.handler(nettyClientInitializer);
            doConnect(session, dataContent);
            doClose();
            doClear(session);

        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Something occurs error in Netty Client: {}", e.getMessage());
        }
    }

    /**
     * 关闭或者结束时清理请求
     *
     * @param session
     */
    private void doClear(WebSocketSession session) {
        channelFuture.addListener(ctl -> {
            String id = session.getId();
            // 各个缓存清除数据
        });
    }

    /**
     * 注册关闭事件
     */
    private void doClose() {
        channelFuture.channel().closeFuture().addListener(cfl -> {
            if(clientChannel!=null){
                clientChannel.close();
            }
//            if (group != null) {
//                group.shutdownGracefully();
//                log.info("channel关闭了");
//            }
            log.info("客户端[" + channelFuture.channel().localAddress().toString() + "]已断开...");
        });
    }

    /**
     * 处理连接事件，及连接后的业务处理
     *
     * @param session 会话
     * @param dataContent 数据
     * @throws InterruptedException 异常
     */
    private void doConnect(WebSocketSession session, RequestDataDto dataContent) throws InterruptedException {
        channelFuture = bootstrap.connect(getHost(), getPort()).sync();

        //注册连接事件
        channelFuture.addListener((ChannelFutureListener)future -> {
            //如果连接成功
            if (future.isSuccess()) {
                chooseChannelHandlerContext(nettyClientInitializer);
                CtxWithWebSocketSessionCache.put(currentCtx, session);
                sendMessage(currentCtx, dataContent);
                log.info("服务端[" + channelFuture.channel().localAddress().toString() + "]已连接...");
                clientChannel = channelFuture.channel();
            }
            //如果连接失败，尝试重新连接
            else{
                log.info("服务端[" + channelFuture.channel().localAddress().toString() + "]连接失败，重新连接中...");
                future.channel().close();
                bootstrap.connect(getHost(), getPort());
            }
        });
    }

    private void chooseChannelHandlerContext(NettyClientInitializer nettyClientInitializer) {
        if (nettyClientInitializer instanceof TcpRequestInitializer) {
            currentCtx = TcpRequestHandler.myCtx;
        } else if (nettyClientInitializer instanceof WebSocketInitializer) {
            // TODO 同上判断类型 以及http
//            currentCtx = WebSocketHandler.myCtx;
        }
        else if (nettyClientInitializer instanceof HttpRequestInitializer) {
          currentCtx = HttpRequestHandler.myCtx;
        }
    }

    /**
     * 往第三方接口系统发送数据
     *
     * @param currentCtx
     * @param dataContent
     */
    private void sendMessage(ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        byte[] serialize = SerializeUtil.serialize(dataContent);
        ByteBuf buffer = currentCtx.alloc().buffer();
        buffer.writeBytes(serialize);
        log.info("客户端往服务端发送的数据：" + dataContent);
        currentCtx.writeAndFlush(buffer);
    }
}
