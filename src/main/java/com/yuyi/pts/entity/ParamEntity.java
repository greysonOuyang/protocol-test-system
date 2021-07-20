//package com.yuyi.pts.entity;
//
//import com.yuyi.pts.common.enums.FieldType;
//import lombok.Data;
//
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
///**
// * description
// *
// * @author greyson
// * @since 2021/7/19
// */
//@Entity
//@Table(name = "t_param")
//@Data
//public class ParamEntity {
//    /**
//     * 主键
//     */
//    private Integer paramId;
//
//    /**
//     * 关联id,与interfaceConfig表id对应
//     */
//    private String interfaceId;
//
//    /**
//     * 写入下标
//     */
//    private Integer index;
//
//    /**
//     * 参数长度
//     */
//    private Integer length;
//
//    /**
//     * 参数名称
//     */
//    private String field;
//
//    /**
//     * 参数值
//     */
//    private String value;
//
//    /**
//     * 参数类型
//     */
//    private FieldType type;
//
//    /**
//     * 类型为输入、输出
//     */
//    private String paramIo;
//}
