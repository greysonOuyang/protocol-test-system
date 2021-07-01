package com.yuyi.pts.netty.handler;

import com.yuyi.pts.netty.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端监测连接是否断开，如果断开则进行重新连接
 *
 * @author greyson
 * @since 2021/6/8
 */
@Slf4j
public class ChannelInactiveHandler extends ChannelInboundHandlerAdapter {


    NettyClient client;

    public ChannelInactiveHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开了，尝试重连");
        client.doConnect();
    }
}
