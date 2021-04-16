package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Service;

/**
 * TCP请求处理器初始化
 *
 * @author greyson
 * @since 2021/4/13
 */
@Service
public class TcpRequestInitializer extends NettyClientInitializer {

    public TcpRequestHandler TCP_HANDLER = new TcpRequestHandler();


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new IdleStateHandler(3, 0, 0))
//                .addLast(new JsonObjectDecoder())
//                .addLast(new StringDecoder(Charset.forName("utf-8")))
                .addLast(TCP_HANDLER);
    }
}
