package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.common.cache.LocalDataRequestOptions;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.netty.handler.TcpRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

/**
 * 抽象类，为了帮助根据协议类型选择Initializer
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public abstract class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
}
