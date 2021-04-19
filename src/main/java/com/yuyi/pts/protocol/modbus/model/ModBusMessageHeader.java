package com.yuyi.pts.protocol.modbus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author greyson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessageHeader {

    /**
     * 业务标识符
     */
    private String affairIdentification;
    /**
     * 协议标识符
     */
    private String protocolIdentification;
    /**
     * 长度标识符
     */
    private String length;
    /**
     * 单元标识符
     */
    private String unitIdentification;

}
