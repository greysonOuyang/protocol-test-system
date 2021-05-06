package com.yuyi.pts.common.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

/**
 * <pre>
 * 基本数据类型转换(主要是byte和其它类型之间的互转).
 * </pre>
 *
 * @author F.Fang
 * @version $Id: ByteUtils.java, v 0.1 2014年11月9日 下午11:23:21 F.Fang Exp $
 */
public class ByteUtils {

    public static byte[] strToBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] convertHEXString2ByteArray(String value) {
        if (value == null || value.length() == 0) {
            return null;
        } else {
            char[] array = value.toCharArray();
            int ext = array.length % 2; // can be 0 or 1 only!
            byte[] out = new byte[array.length / 2 + ext];
            for (int i = 0; i < array.length - ext; i += 2) {
                String part = new String(array, i, 2);
                try {
                    out[i / 2] = (byte) Integer.parseInt(part, 16);
                } catch (NumberFormatException e) {
                    // ignore conversion error
                    out[i / 2] = 0;
                }
            }

            if (ext != 0) {
                String part = String.valueOf(array[array.length - 1]);
                try {
                    out[out.length - 1] = (byte) Integer.parseInt(part, 16);
                } catch (NumberFormatException e) {
                    // ignore conversion error
                    out[out.length - 1] = 0;
                }
            }
            return out;
        }
    }

    /**
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            if ("0X".equals(hex.substring(0, 2))) {
                hex = hex.substring(2);
            }
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * 将一个float数字转换为4个byte数字组成的数组.
     */
    public static byte[] floatToByte4(float f) {
        int i = Float.floatToIntBits(f);
        return intToByte4(i);
    }

    /**
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
     *
     * @param sr
     * @return
     */
    public static int binaryStringToInt(String sr) {
        StringBuffer stringBuffer = new StringBuffer(sr);
        String s = stringBuffer.reverse().toString();
        char[] chars = s.toCharArray();
        int alarmnum = 0;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '1') {
                int j = i;
                int plus = 1;
                while (j-- > 0) {
                    plus *= 2;
                }
                alarmnum += plus;
            }
        }
        return alarmnum;
    }


    /**
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

    /**
     * byte 与 int 的相互转换
     *
     * @param x
     * @return
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytesLow(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytesHigh(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;

    }

    /**
     * byte 与 int 的相互转换
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    /**
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

    /**
     * @param arr arr
     * @return short
     */
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
     * <pre>
     * 将长度为8的8位byte数组转换为64位long.
     * </pre>
     * <p>
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
     * byte数组转字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }

    /**
     * byte[]转十六进制String
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
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

    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }

    public static String byteArrToBinStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            byte b1 = b[i];
            for (int j = 7; j >= 0; j--) {
                int a = (b1 >> j) & 0x01;
                result.append(a);
            }
        }
        return result.toString();
    }

    public static Object bytesToObject(byte[] bytes) {
        if (bytes.length == 2) {
            byte[] valueByte = new byte[2];
            valueByte[0] = bytes[1];
            valueByte[1] = bytes[0];
            return byte2ToShort(valueByte);
        } else if (bytes.length == 4) {
            return byte4ToInt(bytes);
        } else if (bytes.length == 1) {
            return bytes[0];
        }
        return bytes;
    }

    /**
     * 数字字符串转ASCII码字符串
     *
     * @param content 字符串
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }


    /**
     * ascii字符串转成byte数组
     */
    public static byte[] asciiStrToBytes(String str) {
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    public static String bytesToASCII(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte var : bytes) {
            buffer.append((char) var);
        }
        return buffer.toString();
    }

    public static String bytesToDate(byte[] bytes) {
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
     * 将byte数组存入指定大小的byte数组
     *
     * @param source 原数组
     * @param size   指定大小
     * @return 新数组
     */
    public static byte[] storeInBytes(byte[] source, int size) {
        byte[] target = new byte[size];
        return storeInBytes(source, target);
    }

    /**
     * 将一个数组存入另一个数组 如果原数组容量大于目标数组，则截取高位 默认取高位存取
     *
     * @param source 原数组
     * @param target 目标数组
     * @return 新数组
     */
    public static byte[] storeInBytes(byte[] source, byte[] target) {
        return storeInBytes(source, target, true);

    }

    /**
     * 将byte数组存入指定大小的byte数组
     *
     * @param source 原数组
     * @param size   指定大小
     * @return 新数组
     */
    public static byte[] storeInBytesLow(byte[] source, int size) {
        byte[] target = new byte[size];
        return storeInBytesLow(source, target);
    }

    public static byte[] storeInBytesLow(byte[] source, byte[] target) {
        return storeInBytes(source, target, false);

    }

    /**
     * @param source 原数组
     * @param target 目标数组
     * @param isHigh 是否取高位 默认为true
     * @return byte数组
     */
    public static byte[] storeInBytes(byte[] source, byte[] target, boolean isHigh) {
        if (source == null || target == null) {
            return null;
        }
        if (source.length == target.length) {
            return source;
        }
        if (isHigh) {
            if (source.length > target.length) {
                System.arraycopy(source, 0, target, 0, target.length);
            } else {
                System.arraycopy(source, 0, target, 0, source.length);
            }
        } else {
            Stack<Byte> stack = new Stack<Byte>();
            for (byte bt : source) {
                stack.push(bt);
            }
            for (int i = target.length - 1; i >= 0; i--) {
                target[i] = stack.pop();
            }
        }
        return target;
    }


    /**
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 二进制字符串转int
     *
     * @param chars
     * @return
     */
    public static int binarystr2Int(char[] chars) {
        StringBuffer stringBuffer = new StringBuffer();

        int res = 0;
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            int plus = 1;
            int j = i;
            while (j-- > 0) {
                plus *= 2;
            }
            if (chars[i] == '1') {
                res += plus;
            }
        }
        return res;
    }

}