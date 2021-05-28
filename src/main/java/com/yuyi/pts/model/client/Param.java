package com.yuyi.pts.model.client;

import java.io.Serializable;

import com.yuyi.pts.common.util.InstanceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * param
 *
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param implements Serializable {
    public static final InstanceFactory<Param> paramFactory = InstanceFactory.init(Param.class);
    /**
     * 主键
     */
    private Integer paramId;

    /**
     * 关联id,与interfaceConfig表id对应
     */
    private String paramInterfaceId;

    /**
     * 标识id
     */
    private String paramKeyId;

    /**
     * 写入下标
     */
    private Integer paramIndex;

    /**
     * 参数长度
     */
    private Integer paramLength;

    /**
     * 参数名称
     */
    private String paramField;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 输入输出
     */
    private String paramIo;

    private Integer extend3;

    private Integer extend4;

    private Integer extend5;

    private Integer extend6;

    private Integer extend7;

    private static final long serialVersionUID = 1L;

    public Param(Param param) {
        this.paramId = param.paramId;
        this.paramInterfaceId = param.paramInterfaceId;
        this.paramKeyId = param.paramKeyId;
        this.paramIndex = param.paramIndex;
        this.paramLength = param.paramLength;

        this.paramField = param.paramField;
        this.paramValue = param.paramValue;
        this.paramType = param.paramType;
        this.paramIo = param.paramIo;
        this.extend3 = param.extend3;

        this.extend4 = param.extend4;
        this.extend5 = param.extend5;
        this.extend6 = param.extend6;
        this.extend7 = param.extend7;
    }

}