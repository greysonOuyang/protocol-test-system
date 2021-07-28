package com.yuyi.pts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * t_project和t_message_type关联表
 *
 * @author greyson
 * @since 2021/7/27
 */
@Entity
@Table(name = "t_project_message_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithMessageTypeEntity {

    /**
     * 消息类型归属
     */
    @Id
    private Integer messageBelongId;

    private String messageDesc;

    /**
     * 项目ID
     */
    private Integer projectId;

}
