package com.yuyi.pts.common.util;

/**
 * 进制转换
 *
 * @author greyson
 * @since 2021/6/10
 */
public class BaseConversionUtil {
    /**
     * 十进制转十六进制
     *
     * @param decimal_str
     * @return
     */
    public static String decimalToHex(String decimal_str) {
        return Integer.toHexString(Integer.parseInt(decimal_str));
    }

    /**
     * 16进制打印
     *
     * @param bytes
     * @return
     */
    public static String binaryFormat(byte[] bytes) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        int bit;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            bit = (bytes[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bytes[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(" ");
        }
        // 这里的1代表正数
        return sb.toString();
    }
}
