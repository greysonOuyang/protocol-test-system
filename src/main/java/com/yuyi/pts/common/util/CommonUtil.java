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
}
