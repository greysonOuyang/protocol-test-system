package com.yuyi.pts.netty.client;

import com.yuyi.pts.common.cache.CtxWithRequestDataCCache;
import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.handler.HttpRequestInitializer;
import com.yuyi.pts.netty.client.handler.NettyClientInitializer;
import com.yuyi.pts.netty.client.handler.TcpRequestInitializer;
import com.yuyi.pts.netty.client.handler.WebSocketInitializer;
import com.yuyi.pts.netty.handler.HttpRequestHandler;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import com.yuyi.pts.service.ProcessRequestService;
import io.netty.bootstrap.Bootstrap;
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

    @Value("${user.protocol.ip}")
    private String host;

    @Value("${user.protocol.port}")
    private int port;

    @Autowired
    private NettyClientInitializer nettyClientInitializer;

    @Autowired
    private TcpRequestHandler tcpRequestHandler;

    @Autowired
    private ProcessRequestService processRequestService;

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

    /**
     * 启动前需要初始化nettyClientInitializer、host、port
     *
     * @param session 会话
     * @param dataContent 数据
     */
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
     * @param session 会话
     */
    private void doClear(WebSocketSession session) {
        channelFuture.addListener(ctl -> {
            String id = session.getId();
//            session.close();
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
                CtxWithRequestDataCCache.put(currentCtx, dataContent);
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



}
