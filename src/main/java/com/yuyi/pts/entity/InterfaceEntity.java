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
@Table(name = "t_interface")
@Data
public class InterfaceEntity {
    @Id
    @GeneratedValue
    private Integer interfaceId;
    private Integer projectId;
    private String interfaceName;
    private Integer messageTypeId;
}
