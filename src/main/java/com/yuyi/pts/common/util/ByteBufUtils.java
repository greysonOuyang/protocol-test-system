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
    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }
    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
}
