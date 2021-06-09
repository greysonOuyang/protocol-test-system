package com.yuyi.pts.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * t_interface_config
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TInterfaceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer keyId;

    /**
     * 解码类型
     */
    private Integer decoderType;

    /**
     * 编码类型
     */
    private Integer encoderType;

    /**
     * 唯一标识符
     */
    private String interfaceConfigId;

    /**
     * 请求名称
     */
    private String requestName;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求内容
     */
    private String content;

    /**
     * 端口号
     */
    private Integer port;

    private String description;

    /**
     * 客户端/服务端
     */
    private String currentmode;

    /**
     * 输入参数列表
     */
    private List<Param> input;

    /**
     * 输出参数列表
     */
    private List<Param> output;

}