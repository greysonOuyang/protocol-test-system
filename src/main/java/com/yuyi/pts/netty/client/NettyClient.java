package com.yuyi.pts.netty.client;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.netty.client.handler.*;
import com.yuyi.pts.netty.client.initializer.*;
import com.yuyi.pts.service.RequestService;
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
    private RequestService requestService;

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
    public void start(RequestType type,WebSocketSession session, RequestDataDto dataContent) {
        try {
            if (nettyClientInitializer == null) {
                log.error("未能成功初始化NettyClientInitializer");
            } else {
                log.info("当前NettyClientInitializer类型为：" + nettyClientInitializer);
            }
            bootstrap.handler(nettyClientInitializer);
            for (int i = 0; i < dataContent.getCount(); i++) {
                doConnect(session, dataContent);
                chooseChannelHandlerContext(nettyClientInitializer);
                doProcess(type,session, dataContent);
                // TODO 何时调用关闭待确定
                doClose();
                doClear(session);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Something occurs error in Netty Client: {}", e.getMessage());
        }
    }

    /**
     * 业务处理
     *
     * @param session
     * @param dataContent
     */

    private void doProcess(RequestType type,WebSocketSession session, RequestDataDto dataContent) {
        requestService.sendBinMessage(type,currentCtx, dataContent);
        CtxWithWebSocketSessionCache.put(currentCtx, session);
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
        log.info("服务端[" + channelFuture.channel().localAddress().toString() + "连接后");

        //注册连接事件
        channelFuture.addListener((ChannelFutureListener)future -> {
            //如果连接成功
            if (future.isSuccess()) {
                if (currentCtx != null) {
                    log.info("拿到ctx了");
                } else {
                    log.info("拿不到ctx");
                }
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

    /**
     * 选择真正处理请求的协议处理器
     *
     * @param nettyClientInitializer 处理器加载器
     */
    private void chooseChannelHandlerContext(NettyClientInitializer nettyClientInitializer) {
        if (nettyClientInitializer instanceof TcpRequestInitializer) {
            currentCtx = TcpRequestHandler.myCtx;
        } else if (nettyClientInitializer instanceof WebSocketInitializer) {
            currentCtx = WebSocketRequestHandler.myCtx;
        } else if (nettyClientInitializer instanceof HttpRequestInitializer) {
          currentCtx = HttpRequestHandler.myCtx;
        } else if (nettyClientInitializer instanceof ModBusRequestInitializer) {
            currentCtx = ModbusRequestHandler.myCtx;
        }else if (nettyClientInitializer instanceof UdpRequestInitializer) {
            currentCtx = UdpRequestHandler.myCtx;
        } else if (nettyClientInitializer instanceof GzIscsInitializer) {
            currentCtx = GzIscsRequestHandler.myCtx;
        }
    }
}
