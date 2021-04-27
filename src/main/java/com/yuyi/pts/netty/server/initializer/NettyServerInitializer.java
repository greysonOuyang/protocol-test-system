package com.yuyi.pts.netty.server.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.stereotype.Component;

/**
 * @author : wzl
 * @date : 2021/4/27/9:28
 * @description: 抽象类，为了帮助根据协议类型选择Initializer，即协议处理器
 */
@Component
public abstract class NettyServerInitializer<T extends Channel> extends ChannelInitializer<T> {
}
