package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/19
 */
@Entity
@Table(name = "t_request")
@Data
public class RequestEntity {
    /**
     * 唯一标识符
     */
    @Id
    @GeneratedValue
    private Integer requestId;

    /**
     * 编码器
     */
    private String encoderId;
    /**
     * 解码器
     */
    private String decoderId;

    /**
     * 子协议类型
     */
    private String protocolType;

    /**
     * 请求名称
     */
    private String requestName;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求地址url或host
     */
    private String address;

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

}
