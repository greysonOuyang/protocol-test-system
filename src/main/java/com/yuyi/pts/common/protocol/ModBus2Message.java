package com.yuyi.pts.common.protocol;


import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 包含两部分 上部分为Header、下部分为数据字段, 功能码是必选的，依据功能码选择对应的field
 *
 *
 *
 *
 * @author greyson
 */
@Data
@Component
public class ModBus2Message {

    /**
     * 协议头 原始数据 为了方便编码的时候直接原样写回
     */
    private byte[] headerBytes;

    /**
     * 消息体
     */
    private byte[] bodyData;


    /**
     * 协议头，解析后
     */
    private ModBusHeader header;


    /**
     * 数据，解析后
     */
    private ModBusBody body;


}
