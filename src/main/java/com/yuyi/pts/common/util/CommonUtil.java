package com.yuyi.pts.common.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/30
 */
public class CommonUtil {
    public static <T> T mapToJavaBean(Map map, Class<T> clazz) {
        String jsonString = JSON.toJSONString(map);
        return JSON.parseObject(jsonString, clazz);
    }

    public static String randomInt256Str() {
        int num = 1 + (int) (Math.random() * 255);
        return String.valueOf(num);
    }
    public static String random1To18IntStr() {
        int num = 1 +  (int) (Math.random() * 17);
        return String.valueOf(num);
    }
    public static String random1To10IntStr() {
        int num = 1 +  (int) (Math.random() * 9);
        return String.valueOf(num);
    }


}
