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
    public static String remove(List<Map> idList) {
        int countStart = INTERFACE_MAP.size();
        for (Map id : idList) {
            String interfaceId = id.get("id").toString();
            if (id.toString() == null) {
                return null;
            }
            INTERFACE_MAP.remove(interfaceId);
        }
        int countEnd = INTERFACE_MAP.size();
        int number = countStart - countEnd;
        if(number>=1){
            return ResultEntity.successWithData(number);
        }else{
            return ResultEntity.failedWithData("删除失败，请重新尝试");
        }
    }
}
