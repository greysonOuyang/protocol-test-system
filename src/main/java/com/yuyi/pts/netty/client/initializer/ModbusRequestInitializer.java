package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.handler.ModbusRequestHandler;
import com.yuyi.pts.protocol.SmartCarDecoder;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author : wzl
 * @date : 2021/4/16/14:21
 * @description:  modbusTcp的初始化信息
 */
public class ModbusRequestInitializer extends NettyClientInitializer{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new IdleStateHandler(3, 0, 0),
                        // 自定义解码
                        new SmartCarDecoder(),
                        //自定义的
                        new ModbusRequestHandler());
    }
}
