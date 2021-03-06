package com.yuyi.pts.model.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ServiceInterface {
    /**
     * 消息类型 对应枚举类InterfaceMessageType
     */
    private String interfaceType;
    private String interfaceId;
    private String interfaceName;
    private String currentMode;
    private String paramId;
    /**
     * 输入参数列表
     */
    private List<Param> input;

    /**
     * 输出参数列表
     */
    private List<Param> output;
}
