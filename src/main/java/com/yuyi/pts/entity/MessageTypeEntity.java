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
 * @since 2021/7/20
 */
@Entity
@Table(name = "t_message_type")
@Data
public class MessageTypeEntity {

    @Id
    @GeneratedValue
    private Integer messageId;

    private Integer projectId;

    private String projectName;

    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 消息类型描述
     */
    private String messageDescription;
}
