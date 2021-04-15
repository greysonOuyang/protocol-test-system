package com.yuyi.pts.common.util;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

/**
 * 用于序列化、反序列化，后续可能通过枚举扩展可选择算法
 *
 * @author greyson
 * @since 2021/4/15
 */
public class SerializeUtil {

    /** 反序列化方法 */
    public static  <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);

    }

    /** 序列化方法 */
   public static  <T> byte[] serialize(T object) {
       String json = JSON.toJSONString(object);
        return json.getBytes(StandardCharsets.UTF_8);
   }

}
