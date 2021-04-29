package com.yuyi.pts.model.server;

import com.yuyi.pts.common.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {
    /**
     * 写入下标
     */
    private int index;
    /**
     * 写入字节长度
     */
    private int length;
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

}
