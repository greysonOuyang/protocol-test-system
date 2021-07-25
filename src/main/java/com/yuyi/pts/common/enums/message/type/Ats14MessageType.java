package com.yuyi.pts.common.enums.message.type;


import com.yuyi.pts.common.util.Desc;

import java.util.stream.Stream;

/**
 * 综合监控客户端能接收的消息类型
 *
 * @author greyson
 * @since 2021/5/13
 */
@Desc(value = "14号线ATS信息",id = "14")
public enum Ats14MessageType {
    /**
     * 心跳信息
     */
    HEART_BEAT("0x01","心跳信息"),
    TRAIN_INFO("0x02","列车信息"),
    PLAN_INFO("0x03","计划信息"),
    TRAIN_SCHEDULE("0x04","首末班信息"),
    DOOR_ISOLATION_STATUS("0x05","车门隔离状态信息"),
    STATION_ISOLATION_STATUS("0x06","站台门隔离状态信息"),
   ;

    /**
     * 消息类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String description;


    Ats14MessageType(String type, String description) {
        this.description = description;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }

    public static Stream<Ats14MessageType> stream() {
        return Stream.of(Ats14MessageType.values());
    }

}
