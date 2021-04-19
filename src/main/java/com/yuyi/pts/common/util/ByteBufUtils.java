package com.yuyi.pts.common.util;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class ByteBufUtils {
    public static ByteBuf getByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(bytes);
        byteBuf.writeBytes(bytes);
        return buffer;
    }
    public static ByteBuf getByteBuf(byte[] bytes){
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }
    public static void getByteBufString(ByteBuf buf){
        ByteBuf byteBuf = getByteBuf(buf);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        String s = ByteUtils.binaryFormat(bytes);
    }
}
