package com.yuyi.pts.protocol.modbus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author greyson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessage {

    private ModBusMessageHeader modBusMessageHeader;

    private ModBusMessageBody modBusMessageBody;

    /**
     * 业务标识符
     */
    @Value("${user.protocol.affairIdentification}")
    private String affairIdentification;
    /**
     * 协议标识符
     */
    @Value("${user.protocol.protocolIdentification}")
    private String protocolIdentification;
    /**
     * 长度标识符
     */
    @Value("${user.protocol.length}")
    private String length;
    /**
     * 单元标识符
     */
    @Value("${user.protocol.unitIdentification}")
    private String unitIdentification;

    /**
     * 功能码字段
     */
    @Value("${user.protocol.code}")
    private String code;

    /**
     * 数据字段
     */
    private String body;

}
