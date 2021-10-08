package com.yuyi.pts.testServer;

import com.yuyi.pts.common.util.ArrayUtils;
import com.yuyi.pts.common.util.ByteUtils;

import java.util.Arrays;

/**
 * description
 *
 * @author greyson
 * @since 2021/10/8
 */
public class Testmain {
    public static void main(String[] args) {
        char[] chars = {'0','1','0','0','1','1','0','0','0','1','1','0','0','1','1','0'};
        System.out.println(chars.length);
        char[] spilt = ArrayUtils.spilt(chars, 1, 8);
        System.out.println(Arrays.toString(spilt));
        int i = ByteUtils.charsToDecimal(spilt);
        System.out.println(i);
        ArrayUtils.reverse(spilt);
        System.out.println(Arrays.toString(spilt));
        int i2 = ByteUtils.charsToDecimal(spilt);
        System.out.println(i2);

    }
}
