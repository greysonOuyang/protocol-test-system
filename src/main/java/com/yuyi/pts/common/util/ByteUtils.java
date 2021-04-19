package com.yuyi.pts.common.util;

import java.math.BigInteger;

/**
 * ByteUtils
 */

/**
 * 
 * <pre>
 * 基本数据类型转换(主要是byte和其它类型之间的互转).
 * </pre>
 *
 * @author F.Fang
 * @version $Id: ByteUtils.java, v 0.1 2014年11月9日 下午11:23:21 F.Fang Exp $
 */
public class ByteUtils {
    
    /**
     * 
     * <pre>
     * 将4个byte数字组成的数组合并为一个float数.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static float byte4ToFloat(byte[] arr) {
        if (arr == null || arr.length != 4) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
        }
        int i = byte4ToInt(arr);
        return Float.intBitsToFloat(i);
    }
    
    /**
     * 二进制字符串转换成数字
     * @param sr
     * @return
     */
    public static int binaryStringToInt(String sr){
        StringBuffer stringBuffer = new StringBuffer(sr);
        String s = stringBuffer.reverse().toString();
        char[] chars = s.toCharArray();
        int alarmnum = 0;
        for (int i = 0;i<chars.length;i++){
            char aChar = chars[i];
            if(aChar=='1'){
                int j = i;
                int plus = 1;
                while(j-->0){
                    plus *=2;
                }
                alarmnum += plus;
            }
        }
        return alarmnum;
    }

    /**
     * 
     * <pre>
     * 将一个float数字转换为4个byte数字组成的数组.
     * </pre>
     * 
     * @param f
     * @return
     */
    public static byte[] floatToByte4(float f) {
        int i = Float.floatToIntBits(f);
        return intToByte4(i);
    }

    /**
     * 
     * <pre>
     * 将八个byte数字组成的数组转换为一个double数字.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static double byte8ToDouble(byte[] arr) {
        if (arr == null || arr.length != 8) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
        }
        long l = byte8ToLong(arr);
        return Double.longBitsToDouble(l);
    }

    /**
     * 
     * <pre>
     * 将一个double数字转换为8个byte数字组成的数组.
     * </pre>
     * 
     * @param i
     * @return
     */
    public static byte[] doubleToByte8(double i) {
        long j = Double.doubleToLongBits(i);
        return longToByte8(j);
    }

    /**
     * 
     * <pre>
     * 将一个char字符转换为两个byte数字转换为的数组.
     * </pre>
     * 
     * @param c
     * @return
     */
    public static byte[] charToByte2(char c) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (c >> 8);
        arr[1] = (byte) (c & 0xff);
        return arr;
    }

    /**
     * 
     * <pre>
     * 将2个byte数字组成的数组转换为一个char字符.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static char byte2ToChar(byte[] arr) {
        if (arr == null || arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return (char) (((char) (arr[0] << 8)) | ((char) arr[1]));
    }

    /**
     * 
     * <pre>
     * 将一个16位的short转换为长度为2的8位byte数组.
     * </pre>
     * 
     * @param s
     * @return
     */
    public static byte[] shortToByte2(Short s) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (s >> 8);
        arr[1] = (byte) (s & 0xff);
        return arr;
    }

    // 16进制打印
    public static String binaryFormat(byte[] bytes){
        char[] chars = "0123456789ABCDEF".toCharArray();
        int bit;
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<bytes.length;i++){
            byte aByte = bytes[i];
            bit = (bytes[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bytes[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(" ");
        }
        return sb.toString();// 这里的1代表正数
    }


    /**
     * 
     * <pre>
     * 长度为2的8位byte数组转换为一个16位short数字.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static short byte2ToShort(byte[] arr) {
        if (arr != null && arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return (short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
    }

    /**
     * 
     * <pre>
     * 将short转换为长度为16的byte数组.
     * 实际上每个8位byte只存储了一个0或1的数字
     * 比较浪费.
     * </pre>
     * 
     * @param s
     * @return
     */
    public static byte[] shortToByte16(short s) {
        byte[] arr = new byte[16];
        for (int i = 15; i >= 0; i--) {
            arr[i] = (byte) (s & 1);
            s >>= 1;
        }
        return arr;
    }

    public static short byte16ToShort(byte[] arr) {
        if (arr == null || arr.length != 16) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度为16!");
        }
        short sum = 0;
        for (int i = 0; i < 16; ++i) {
            sum |= (arr[i] << (15 - i));
        }
        return sum;
    }

    /**
     * 
     * <pre>
     * 将32位int转换为由四个8位byte数字.
     * </pre>
     * 
     * @param sum
     * @return
     */
    public static byte[] intToByte4(int sum) {
        int i = sum & 0xff;
        byte[] arr = new byte[4];
        arr[0] = (byte) (sum >> 24);
        arr[1] = (byte) (sum >> 16);
        arr[2] = (byte) (sum >> 8);
        arr[3] = (byte) (sum & 0xff);
        return arr;
    }

    /**
     * <pre>
     * 将长度为4的8位byte数组转换为32位int.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static int byte4ToInt(byte[] arr) {
        if (arr == null || arr.length != 4) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
        }
        return (int) (((arr[0] & 0xff) << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | ((arr[3] & 0xff)));
    }

    /**
     * 
     * <pre>
     * 将长度为8的8位byte数组转换为64位long.
     * </pre>
     * 
     * 0xff对应16进制,f代表1111,0xff刚好是8位 byte[]
     * arr,byte[i]&0xff刚好满足一位byte计算,不会导致数据丢失. 如果是int计算. int[] arr,arr[i]&0xffff
     * 
     * @param arr
     * @return
     */
    public static long byte8ToLong(byte[] arr) {
        if (arr == null || arr.length != 8) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
        }
        return (long) (((long) (arr[0] & 0xff) << 56) | ((long) (arr[1] & 0xff) << 48) | ((long) (arr[2] & 0xff) << 40)
                        | ((long) (arr[3] & 0xff) << 32) | ((long) (arr[4] & 0xff) << 24)
                        | ((long) (arr[5] & 0xff) << 16) | ((long) (arr[6] & 0xff) << 8) | ((long) (arr[7] & 0xff)));
    }

    /**
     * 将一个long数字转换为8个byte数组组成的数组.
     */
    public static byte[] longToByte8(long sum) {
        byte[] arr = new byte[8];
        arr[0] = (byte) (sum >> 56);
        arr[1] = (byte) (sum >> 48);
        arr[2] = (byte) (sum >> 40);
        arr[3] = (byte) (sum >> 32);
        arr[4] = (byte) (sum >> 24);
        arr[5] = (byte) (sum >> 16);
        arr[6] = (byte) (sum >> 8);
        arr[7] = (byte) (sum & 0xff);
        return arr;
    }

    /**
     * 
     * <pre>
     * 将int转换为32位byte.
     * 实际上每个8位byte只存储了一个0或1的数字
     * 比较浪费.
     * </pre>
     * 
     * @param num
     * @return
     */
    public static byte[] intToByte32(int num) {
        byte[] arr = new byte[32];
        for (int i = 31; i >= 0; i--) {
            // &1 也可以改为num&0x01,表示取最地位数字.
            arr[i] = (byte) (num & 1);
            // 右移一位.
            num >>= 1;
        }
        return arr;
    }

    /**
     * 
     * <pre>
     * 将长度为32的byte数组转换为一个int类型值.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static int byte32ToInt(byte[] arr) {
        if (arr == null || arr.length != 32) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度是32!");
        }
        int sum = 0;
        for (int i = 0; i < 32; ++i) {
            sum |= (arr[i] << (31 - i));
        }
        return sum;
    }

    /**
     * 
     * <pre>
     * 将长度为64的byte数组转换为一个long类型值.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static long byte64ToLong(byte[] arr) {
        if (arr == null || arr.length != 64) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度是64!");
        }
        long sum = 0L;
        for (int i = 0; i < 64; ++i) {
            sum |= ((long) arr[i] << (63 - i));
        }
        return sum;
    }

    /**
     * 
     * <pre>
     * 将一个long值转换为长度为64的8位byte数组.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     * 
     * @param sum
     * @return
     */
    public static byte[] longToByte64(long sum) {
        byte[] arr = new byte[64];
        for (int i = 63; i >= 0; i--) {
            arr[i] = (byte) (sum & 1);
            sum >>= 1;
        }
        return arr;
    }

    public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }

    public static String byteArrToBinStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            byte b1 = b[i];
            for(int j = 7;j>=0;j--){
                int a = (b1>>j)&0x01;
                result.append(a);
            }
        }
        return result.toString();
    }
    /*public static byte[] valueToBytes(Object value){
        byte[] bytes;
        if (value instanceof Integer) {
            return intToByte4((int) value);
        }else if (value instanceof Short) {
            return shortToByte2((short) value);
        }else if (value instanceof Byte) {
            bytes = new byte[1];
            bytes[0] = (byte) value;
            return bytes;
        }
        return null;
    }*/
    public static Object bytesToObject(byte[] bytes){
        if (bytes.length == 2) {
            byte[] valueByte = new byte[2];
            valueByte[0] = bytes[1];
            valueByte[1] = bytes[0];
            return byte2ToShort(valueByte);
        }else if (bytes.length == 4) {
            return byte4ToInt(bytes);
        }else if (bytes.length == 1) {
            return bytes[0];
        }
        return bytes;
    }

    public static String bytesToASCII(byte[] bytes){
        StringBuilder buffer = new StringBuilder();
        for (byte var : bytes) {
            buffer.append((char) var);
        }
        return buffer.toString();
    }
    public static String bytesToDate(byte[] bytes){
        byte[] yearBytes = new byte[2];
        yearBytes[0] = bytes[1];
        yearBytes[1] = bytes[0];
        short year = byte2ToShort(yearBytes);
        byte month = bytes[2];
        byte day = bytes[3];
        byte hour = bytes[4];
        byte minute = bytes[5];
        byte second = bytes[6];
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
    /**  
 * Convert hex string to byte[]  
 * @param hexString the hex string  
 * @return byte[]  
 */  
public static byte[] hexStringToBytes(String hexString) {   
    if (hexString == null || hexString.equals("")) {   
        return null;   
    }   
    hexString = hexString.toUpperCase();   
    int length = hexString.length() / 2;   
    char[] hexChars = hexString.toCharArray();   
    byte[] d = new byte[length];   
    for (int i = 0; i < length; i++) {   
        int pos = i * 2;   
        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    }   
    return d;   
}
    public  static int binarystr2Int(char[] chars){
        StringBuffer stringBuffer = new StringBuffer();

        int res = 0;
        int length = chars.length;
        for (int i=0;i<length;i++){
            int plus = 1;
            int j = i;
            while(j-->0){
                plus *=2;
            }
            if(chars[i]=='1'){
                res += plus;
            }
        }
        return res;
    }
/**  
 * Convert char to byte  
 * @param c char  
 * @return byte  
 */   
private static byte charToByte(char c) {   
    return (byte) "0123456789ABCDEF".indexOf(c);   
}  

}