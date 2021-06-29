package com.yuyi.pts.common.protocol;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * ModBUS协议消息头
 *
 * @author greyson
 * @since 2021/6/18
 */
@Data
public class ModBusHeader {
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
    private short length;
    /**
     * 单元标识符
     */
    @Value("${user.protocol.unitIdentification}")
    private String unitIdentification;
}
