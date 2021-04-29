package com.yuyi.pts.common.cache;

import com.yuyi.pts.model.server.ServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wzl
 * @date : 2021/4/29/9:16
 * @description: 缓存excel文件
 */
public class PlanInfoCache {
    public static Map<String, ServiceInterface> mapCache = new HashMap<>();
    public static Map put(String key, ServiceInterface map) {
        if (key == null || map == null) {

        }
        mapCache.put(key,map);
        return mapCache;
    }
    public static  ServiceInterface get(String key) {
        if (key == null) {
            return null;
        }
        return mapCache.get(key);
    }
}
