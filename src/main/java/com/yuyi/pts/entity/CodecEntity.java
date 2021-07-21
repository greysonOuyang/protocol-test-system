package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author greyson
 */
@Entity
@Table(name = "t_codec")
@Data
public class CodecEntity {
    @Id
    @GeneratedValue
    private Integer codecId;
    private String codecName;
    /**
     * 1 编码 0 解码
     */
    private String codecType;
}
