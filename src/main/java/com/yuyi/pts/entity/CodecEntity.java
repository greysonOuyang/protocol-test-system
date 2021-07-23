package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author greyson
 */
@Entity
@Table(name = "t_codec")
@Data
public class CodecEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codecId;

    private String codecName;
    /**
     * 1 编码 0 解码
     */
    private String codecType;

    private String codecDesc;
}
