package com.yuyi.pts.common.util;

/**
 * 数组工具
 *
 * @author greyson
 * @since 2021/7/13
 */
public class ArrayUtils {

    /**
     * 截取数组
     *
     * @param array
     * @param start
     * @param offset
     * @return
     */
    public static byte[] spilt(byte[] array, int start, int offset) {
        byte[] bytes = new byte[offset - start];
        System.arraycopy(array, start, bytes, 0, offset - start);
        return bytes;
    }

    public static char[] spilt(char[] array, int start, int offset) {
        char[] chars = new char[offset - start];
        System.arraycopy(array, start, chars, 0, offset - start);
        return chars;
    }

    /**
     * 数组内容翻转
     *
     * @param array
     */
    public static void reverse(Object[] array) {
        if (array == null) {
            return;
        }

        Object tmp;

        for (int i = 0, j = array.length - 1; j > i; i++, j--) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }
    /**
     * char数组内容翻转
     *
     * @param array
     */
    public static void reverse(char[] array) {
        if (array == null) {
            return;
        }

        char tmp;

        for (int i = 0, j = array.length - 1; j > i; i++, j--) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }
}
