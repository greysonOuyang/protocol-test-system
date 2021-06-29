package com.yuyi.pts.testServer;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/27
 */
public class TestMain
{
    public static void main(String[] args) {
//        byte tByte = 85;
//        String tString = Integer.toBinaryString((tByte & 0xFF) + 0x100).substring(1);
//        System.out.println(tString);
        int num = 241;
        byte[] bytes = fromShort(241);
        System.out.println(bytes);
        short i = arr2short(bytes);
        System.out.println(i);
    }

    public static short arr2short(byte[] arr) {
        int i = 0;
        i |= arr[0] & 0xFF;
        i <<= 8;
        i |= arr[1] & 0xFF;
        return (short) i;
    }

    public static byte[] fromShort(int shortValue) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (shortValue >> 8);
        bytes[1] = (byte) ((shortValue << 8) >> 8);
        return bytes;
    }
}
