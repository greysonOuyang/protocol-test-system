package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.client.codec.ModBusCodec;
import com.yuyi.pts.netty.client.handler.ModbusRequestHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author : wzl
 * @date : 2021/4/16/14:21
 * @description:  modbusTcp的初始化信息
 */
public class ModBusRequestInitializer extends NettyClientInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        // 自定义解码
                        new ModBusCodec(),
                        new IdleStateHandler(3, 0, 0),
                        //自定义的处理器
                        new ModbusRequestHandler());
    }
}
