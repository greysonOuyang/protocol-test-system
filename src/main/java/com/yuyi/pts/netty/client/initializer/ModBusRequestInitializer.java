package com.yuyi.pts.netty.client.initializer;

import com.yuyi.pts.netty.handler.ModbusRequestHandler;
import com.yuyi.pts.protocol.modbus.codec.ModBusCodec;
import com.yuyi.pts.protocol.modbus.codec.ProcotolFrameDecoder;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author : wzl
 * @date : 2021/4/16/14:21
 * @description:  modbusTcp的初始化信息
 */
public class ModBusRequestInitializer extends NettyClientInitializer{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        // 自定义解码
//                        new SmartCarDecoder(),
                        new ProcotolFrameDecoder(),
                        new ModBusCodec(),
                        new IdleStateHandler(3, 0, 0),
                        //自定义的处理器
                        new ModbusRequestHandler());
    }
}
