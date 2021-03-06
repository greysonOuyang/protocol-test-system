package com.yuyi.pts.model.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class RequestVo {
    /**
     * 端口
     */
    private int port;

    /**
     * ip地址
     */
    private String host;

    /**
     * 启动模式
     */
    private String startMode;

    /**
     * 数据模式
     */
    private String dataMode;

    /**
     * 数据进制
     */
    private String baseType;

    /**
     * 接口Id
     */
    private Integer interfaceId;

    /**
     * 列车趟数 计划信息
     */
    private String trainCount;

    /**
     * 站台趟数 计划信息
     */
    private String stationCount;

    private String interfaceName;

    private Integer messageTypeId;

    /**
     * 报文
     */
    private String context;

}
