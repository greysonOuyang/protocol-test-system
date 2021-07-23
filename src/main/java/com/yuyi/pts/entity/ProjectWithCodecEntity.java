package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**@Entity
 @Table(name = "t_project")
 @Data
 * description
 *
 * @author greyson
 * @since 2021/7/23
 */
@Entity
@Table(name = "t_project_codec")
@Data
public class ProjectWithCodecEntity {
    /**
     * t_project和t_codec关联表
     */
    @Id
    @GeneratedValue
    private Integer pcId;
    private Integer projectId;
    private Integer codecId;
}
