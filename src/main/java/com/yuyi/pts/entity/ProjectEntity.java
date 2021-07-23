package com.yuyi.pts.entity;

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
@Table(name = "t_project")
@Data
public class ProjectEntity {
    @Id
    @GeneratedValue
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 编码器
     */
    private Integer encoderId;
    /**
     * 解码器
     */
    private Integer decoderId;

}
