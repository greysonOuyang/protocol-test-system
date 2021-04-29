package com.yuyi.pts.common.cache;

import com.yuyi.pts.common.util.ResultEntity;
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

    public static ServiceInterface remove(List<Map> idList) {
        for (Map id : idList) {
            String interfaceId = id.get("id").toString();
            if (id.toString() == null) {
                return null;
            }
            INTERFACE_MAP.remove(interfaceId);
        }
        return null;
    }
}
