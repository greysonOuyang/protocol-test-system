package com.yuyi.pts.config;

import com.yuyi.pts.netty.client.NettyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/25
 */
@Configuration
public class NettyClientConfig {

    @Bean
    NettyClient getNettyNoargs() {
        return new NettyClient();
    }

    @Bean
    NettyClient getNettyUdp() {
        return new NettyClient("udp");
    }
}
