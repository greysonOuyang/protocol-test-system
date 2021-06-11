package com.yuyi.pts.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.stereotype.Component;

/**
 * 抽象类，为了帮助根据协议类型选择Initializer，即协议处理器
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public abstract class AbstractNettyInitializer<T extends Channel> extends ChannelInitializer<T> {
}
