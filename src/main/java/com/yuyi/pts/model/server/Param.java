package com.yuyi.pts.model.server;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.model.excel.ExcelCell;
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
    @ExcelCell(index = 0)
    private int index;
    /**
     * 写入字节长度
     */
    @ExcelCell(index = 1)
    private int length;
    /**
     * 参数名称
     */
    @ExcelCell(index = 2)
    private String field;
    /**
     * 参数值
     */
    @ExcelCell(index = 3)
    private String value;

    /**
     * 参数类型
     */
    @ExcelCell(index = 4)
    private FieldType type;

}
