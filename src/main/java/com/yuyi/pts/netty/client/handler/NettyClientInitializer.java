package com.yuyi.pts.netty.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

/**
 * 抽象类，为了帮助根据协议类型选择Initializer，即协议处理器
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public abstract class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
}
