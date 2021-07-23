package com.yuyi.pts.entity;

import com.yuyi.pts.common.util.InstanceFactory;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/19
 */
@Entity
@Table(name = "t_param")
@Data
public class ParamEntity {

//    public static final InstanceFactory<ParamEntity> paramFactory = InstanceFactory.init(ParamEntity.class);

    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Integer paramId;

    /**
     * 关联id,与interfaceConfig表id对应
     */
    private Integer interfaceId;

    /**
     * 写入下标
     */
    private Integer writeIndex;

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
    private String type;

    /**
     * 类型为输入、输出
     */
    private String paramIo;
}
