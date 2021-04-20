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

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param  str    16进制格式的字符串，注意字符之间不要有空格
     * @return    转换后的字节数组
     */
    public static boolean isEmpty( String str ){
        if( str == null || str.equals( "" ) ){
            return true;
        }else{
            return false;
        }
    }

    public static byte[] toByteArray(String hexString){
        if( isEmpty( hexString ) ){
            throw new IllegalArgumentException("this hexString must not be empty");
        }

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;

        for( int i = 0; i < byteArray.length; i++ ){
            //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) ( high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
}
