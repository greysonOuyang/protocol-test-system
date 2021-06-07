package com.yuyi.pts.model.client;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.InstanceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Integer index;

    /**
     * 参数长度
     */
    private Integer length;

    /**
     * 参数名称
     */
    private String field;

    /**
     * 参数值
     */
    private String value;

    /**
     * 参数类型
     */
    private FieldType type;

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
        this.index = param.index;
        this.length = param.length;

        this.field = param.field;
        this.value = param.value;
        this.type = param.type;
        this.paramIo = param.paramIo;
        this.extend3 = param.extend3;

        this.extend4 = param.extend4;
        this.extend5 = param.extend5;
        this.extend6 = param.extend6;
        this.extend7 = param.extend7;
    }

}