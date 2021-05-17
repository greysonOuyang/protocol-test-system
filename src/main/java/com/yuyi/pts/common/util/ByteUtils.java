package com.yuyi.pts.common.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /**
     * 十进制转十六进制
     *
     * @param decimal_str
     * @return
     */
    public static String decimalToHex(String decimal_str) {
        return Integer.toHexString(Integer.parseInt(decimal_str));
    }

    public static byte[] asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        byte[] target = new byte[chars.length];
        StringBuilder hex_str = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            target[i] = ByteUtils.intToByte((int) chars[i]);
        }
        return target;
    }

    public static byte[] asciiToHex(byte[] in) {
        byte[] temp1 = new byte[1];
        byte[] temp2 = new byte[1];
        int i = 0;
        byte[] out = new byte[in.length / 2];
        for (int j = 0; i < in.length; j++) {
            temp1[0] = in[i];/*w  w w . java 2  s .c o m*/
            temp2[0] = in[(i + 1)];
            if ((temp1[0] >= 48) && (temp1[0] <= 57)) {
                int tmp53_52 = 0;
                byte[] tmp53_51 = temp1;
                tmp53_51[tmp53_52] = (byte) (tmp53_51[tmp53_52] - 48);
                temp1[0] = (byte) (temp1[0] << 4);

                temp1[0] = (byte) (temp1[0] & 0xF0);
            } else if ((temp1[0] >= 97) && (temp1[0] <= 102)) {
                int tmp101_100 = 0;
                byte[] tmp101_99 = temp1;
                tmp101_99[tmp101_100] = (byte) (tmp101_99[tmp101_100] - 87);
                temp1[0] = (byte) (temp1[0] << 4);
                temp1[0] = (byte) (temp1[0] & 0xF0);
            }

            if ((temp2[0] >= 48) && (temp2[0] <= 57)) {
                int tmp149_148 = 0;
                byte[] tmp149_146 = temp2;
                tmp149_146[tmp149_148] = (byte) (tmp149_146[tmp149_148] - 48);

                temp2[0] = (byte) (temp2[0] & 0xF);
            } else if ((temp2[0] >= 97) && (temp2[0] <= 102)) {
                int tmp192_191 = 0;
                byte[] tmp192_189 = temp2;
                tmp192_189[tmp192_191] = (byte) (tmp192_189[tmp192_191] - 87);

                temp2[0] = (byte) (temp2[0] & 0xF);
            }
            out[j] = (byte) (temp1[0] | temp2[0]);
            i += 2;
        }
        return out;
    }

    public static String asciiToBinary(String asciiString) {

        byte[] bytes = asciiString.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    public static byte[] addByteArrays(byte[] array1, byte[] array2) {
        byte[] concatenatedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatenatedArray, 0, array1.length);
        System.arraycopy(array2, 0, concatenatedArray, array1.length, array2.length);
        return concatenatedArray;
    }

    public static byte[] strToBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static void reverseBytes(byte[] array) {
        if (array == null) {
            return;
        }

        byte tmp;

        for (int i = 0, j = array.length - 1; j > i; i++, j--) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }

    /**
     * 十进制字转成十六进制字节数组
     *
     * @param str 字符串
     * @return
     */
    public static byte[] decimalStrToHex(String str) {
        return ByteUtils.convertHEXString2ByteArray(ByteUtils.decimalToHex(str));
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
     * 16进制字符串转字节数组 支持带0x开头
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
     * 将一个16位的short转换为长度为2的8位byte数组. 小端模式
     *
     * @param s
     * @return
     */
    public static byte[] shortToByte2SmallEnd(Short s) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (s & 0xff);
        arr[1] = (byte) (s >> 8);
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
     * 将byte数组存入指定大小的byte数组 截取低位
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
            System.arraycopy(source, 0, target, 0, Math.min(source.length, target.length));
        } else {
            Stack<Byte> stack = new Stack<Byte>();
            for (byte bt : source) {
                stack.push(bt);
            }
            int length = Math.min(source.length, target.length);
            for (int i = length - 1; i >= 0; i--) {
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
    public static byte[] charsToBytes(char[] chars){
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = charset.encode(CharBuffer.wrap(chars));
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }
    /**
     * 字符串转换unicode
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }
}