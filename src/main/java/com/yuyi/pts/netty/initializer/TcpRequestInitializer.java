package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Service;

/**
 * TCP请求处理器初始化
 *
 * @author greyson
 * @since 2021/4/13
 */
@Service
public class TcpRequestInitializer extends AbstractNettyInitializer<SocketChannel> {

    public TcpRequestHandler TCP_HANDLER = new TcpRequestHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new IdleStateHandler(3, 0, 0))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(TCP_HANDLER);
    }
}
