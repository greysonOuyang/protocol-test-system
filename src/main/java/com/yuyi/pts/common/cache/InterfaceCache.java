package com.yuyi.pts.common.cache;

import com.yuyi.pts.model.server.ServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/28
 */
public class InterfaceCache {
    public static Map<String, ServiceInterface>  INTERFACE_MAP = new HashMap<>();

    public static ServiceInterface put(String key, ServiceInterface serviceInterface) {
        if (key == null || serviceInterface == null) {
           return null;
        }
        return   INTERFACE_MAP.put(key, serviceInterface);
    }

    public static ServiceInterface remove(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return null;
        }
        for (String id : idList) {
            INTERFACE_MAP.remove(id);
        }
        return null;
    }

    public static ServiceInterface get(String id) {
        if (id == null) {
            return null;
        }
        return INTERFACE_MAP.get(id);
    }
}
