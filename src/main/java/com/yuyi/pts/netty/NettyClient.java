package com.yuyi.pts.netty;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.netty.handler.*;
import com.yuyi.pts.netty.initializer.*;
import com.yuyi.pts.service.RequestService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

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
    private AbstractNettyInitializer abstractNettyInitializer;

    @Autowired
    private RequestService requestService;

    private ChannelHandlerContext currentCtx;

    private NioEventLoopGroup group;

    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    TInterfaceConfig serviceInterface;


    /**
     * 客户端通道
     */
    private Channel clientChannel;

    public NettyClient() {
        new NettyClient(NioSocketChannel.class);
    }

    public NettyClient(Class clazz) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(clazz);
        bootstrap.group(group);
        if (clazz == NioDatagramChannel.class) {
            bootstrap.option(ChannelOption.SO_BROADCAST,true);
            // 设置UDP读缓冲区为2M
            bootstrap.option(ChannelOption.SO_RCVBUF, 2048 * 1024);
            // 设置UDP写缓冲区为1M
            bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 1024);
        }
    }


    /**
     * 启动前需要初始化nettyClientInitializer、host、port
     *
     * @param session 会话
     * @param dataContent 数据
     */
    public void start(WebSocketSession session, RequestDataDto dataContent) {
        RequestType type = dataContent.getType();
        try {
            if (abstractNettyInitializer == null) {
                log.error("未能成功初始化NettyClientInitializer");
            } else {
                log.info("当前NettyClientInitializer类型为：" + abstractNettyInitializer);
            }
            bootstrap.handler(abstractNettyInitializer);
            // udp不需要建立连接,其他类型需要建立连接 TODO UDP分成客户端和服务端，支持广播和单播
            if(RequestType.UDP.equals(type)){
                doPostAndReceive(session, dataContent);
            } else {
                doConnect();
                chooseChannelHandlerContext(abstractNettyInitializer);
                doProcess(type,session, dataContent);
                // TODO 何时调用关闭待确定
                doClose();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            log.error("Something occurs error in Netty Client: {}", e.getMessage());
        }
    }

    public void start() {
        bootstrap.handler(abstractNettyInitializer);
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  处理udp发送接收数据
     */
    private void doPostAndReceive(WebSocketSession session, RequestDataDto dataContent) throws IOException, InterruptedException {

        //创建于服务端就是连接操作，创建线程
        try {
            //服务端绑定的管道的端口
            Channel ch = null;
            ch = bootstrap.bind(8888).sync().channel();
            // 向网段类所有机器广播发UDP，这是想客户端发送内容
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(dataContent.getBody().toString(), CharsetUtil.UTF_8),
                    //地址
                    new InetSocketAddress(
                            dataContent.getHost(),port
                    ))).sync();
            chooseChannelHandlerContext(abstractNettyInitializer);
            CtxWithWebSocketSessionCache.put(currentCtx, session);
            //如果超过长时间则表示超时
            if(!ch.closeFuture().await(100)){
                session.close();
                System.out.println("查询超时！！！");
            }
        }
        finally {
            //优雅的关闭释放内存
            group.shutdownGracefully();
        }

    }

    /**
     * 业务处理
     *
     * @param session
     * @param dataContent
     */

    private void doProcess(RequestType type, WebSocketSession session, RequestDataDto dataContent) {
        if (type == RequestType.WebSocket) {
            URI websocketURI = null;
            try {
                websocketURI = new URI(dataContent.getUrl());
                HttpHeaders httpHeaders = new DefaultHttpHeaders();
                //进行握手
                WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                        websocketURI, WebSocketVersion.V13, (String)null, true, httpHeaders);
                Channel channel = channelFuture.channel();
                WebSocketRequestHandler handler = (WebSocketRequestHandler) channel.pipeline().get("hookedHandler");
                handler.setHandShaker(handShaker);
                handShaker.handshake(channel);
                //阻塞等待是否握手成功
                handler.handshakeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requestService.sendTextMessage(type, currentCtx, dataContent);
        CtxWithWebSocketSessionCache.put(currentCtx, session);
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
     * @throws InterruptedException 异常
     */
    private void doConnect() throws InterruptedException, IOException {

       channelFuture = bootstrap.connect(getHost(), getPort()).sync();
        log.info("服务端[" + channelFuture.channel().localAddress().toString() + "连接后");

        //注册连接事件
        channelFuture.addListener((ChannelFutureListener) future -> {
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
     * @param abstractNettyInitializer 处理器加载器
     */
    private void chooseChannelHandlerContext(AbstractNettyInitializer abstractNettyInitializer) {
        if (abstractNettyInitializer instanceof TcpRequestInitializer) {
            currentCtx = TcpRequestHandler.myCtx;
        } else if (abstractNettyInitializer instanceof WebSocketInitializer) {
            currentCtx = WebSocketRequestHandler.myCtx;
        } else if (abstractNettyInitializer instanceof HttpRequestInitializer) {
          currentCtx = HttpRequestHandler.myCtx;
        } else if (abstractNettyInitializer instanceof ModBusRequestInitializer) {
            currentCtx = ModbusRequestHandler.myCtx;
        }else if (abstractNettyInitializer instanceof UdpRequestInitializer) {
            currentCtx = UdpRequestHandler.myCtx;
        }
    }

}
