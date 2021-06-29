package com.yuyi.pts.common.protocol;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/21
 */
@Data
public class ModBusBody {

    /**
     * 是否为响应数据 true为是，false为请求数据 默认为请求，在响应数据的时候一定要设置值
     */
    private Boolean isResponse = false;

    /**
     * 功能码字段
     */
    @Value("${user.protocol.code}")
    private String functionCode;

    /**
     * 寄存器读取或者写起始地址
     */
    @Value("${user.protocol.startAddress}")
    private short startAddress;

    /**
     * 寄存器个数
     */
    @Value("${user.protocol.registerCounters}")
    private short registerCounters;

    /**
     * 字节个数 int类型是为了做兼容
     */
    private int byteCount;

    /**
     * 数据字段
     */
    private byte[] data;
}
