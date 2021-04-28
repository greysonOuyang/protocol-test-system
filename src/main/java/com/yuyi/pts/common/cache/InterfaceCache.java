package com.yuyi.pts.common.cache;

import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.model.server.ServiceInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/28
 */
public class InterfaceCache {
    public static Map<String, ServiceInterface> INTERFACE_MAP = new HashMap<>();

    public static String put(String key, ServiceInterface serviceInterface) {
        if (key == null || serviceInterface == null) {
            return ResultEntity.failedWithMsg("输入参数有误");
        }
        ServiceInterface result = INTERFACE_MAP.put(key, serviceInterface);
        return ResultEntity.successWithData(result);
    }

    public static ServiceInterface remove(String key) {
        if (key == null) {
            return null;
        }
        return INTERFACE_MAP.remove(key);
    }
}
