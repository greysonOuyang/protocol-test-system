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
public class ParamEntity implements Serializable {
    public static final InstanceFactory<ParamEntity> paramFactory = InstanceFactory.init(ParamEntity.class);
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

}