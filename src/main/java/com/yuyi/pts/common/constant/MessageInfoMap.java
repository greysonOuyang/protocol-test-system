package com.yuyi.pts.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzl
 * @date:2021-06-09 16:54
 */
public class MessageInfoMap {

    public static Map get18Map(){
        Map map = new HashMap();
        map.put("0x01","心跳信息");
        map.put("0x02","列车信息");
        map.put("0x03","计划信息");
        map.put("0x04","首末班信息");
        map.put("0x05","车门隔离状态信息");
        map.put("0x06","站台隔离状态信息");
        return map;
    }
    public static Map get14Map(){
        Map map = new HashMap();
        map.put("0x01","心跳信息");
        map.put("0x02","站台首末班列车");
        map.put("0x03","站台到站列车信息");
        map.put("0x04","列车到站信息");
        map.put("0x0A","列车满载率信息");
        return map;
    }
}
