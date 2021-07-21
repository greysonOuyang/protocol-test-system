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
@Table(name = "t_config")
@Data
public class ConfigEntity {

    @Id
    @GeneratedValue
    private Integer configId;

    private Integer requestId;

    /**
     * 配置Key
     */
    private String configKey;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置类型
     */
    private String configType;
    /**
     * 选择下拉框的选项
     */
    private String selectionId;

    /**
     * 此配置项的对应值
     */
    private String configValue;
}
