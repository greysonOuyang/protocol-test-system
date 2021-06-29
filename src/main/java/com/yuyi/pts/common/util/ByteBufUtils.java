package com.yuyi.pts.common.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty的 byteBuf 工具
 *
 * @author greyson
 * @since 2021/6/8
 */
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

    /**
     * 解析出一个 short
     *
     * @param byteBuf byteBuf
     * @return short
     */
    public static short parseShort(ByteBuf byteBuf) {
        byte[] bytes = new byte[2];
        byteBuf.readBytes(bytes);
        return ByteUtils.byte2ToShort(bytes);
    }

    /**
     * 解析几个字节
     *
     * @param byteBuf 源
     * @param count 字节数
     * @return byte[]
     */
    public static byte[] parseByteCount(ByteBuf byteBuf, int count) {
        byte[] bytes = new byte[count];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static byte[] parseOne(ByteBuf byteBuf) {
       return parseByteCount(byteBuf, 1);
    }

    public static byte[] parseTwo(ByteBuf byteBuf) {
        return parseByteCount(byteBuf, 2);
    }
}
