package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/23
 */
@Entity
@Table(name = "t_project_codec")
@Data
public class ProjectWithCodecEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    private Integer projectId;

    private Integer CodecId;
}
