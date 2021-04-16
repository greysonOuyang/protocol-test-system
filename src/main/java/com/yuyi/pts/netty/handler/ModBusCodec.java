package com.yuyi.pts.netty.handler;

import com.yuyi.pts.protocol.ModBus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * ModBus协议编码解码器
 *
 * @author greyson
 * @since 2021/4/16
 */
public class ModBusCodec extends MessageToMessageCodec<ByteBuf, ModBus> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ModBus modBus, List<Object> list) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
