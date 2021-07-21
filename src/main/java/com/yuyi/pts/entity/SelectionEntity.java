package com.yuyi.pts.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_selection")
@Data
public class SelectionEntity {
    @Id
    @GeneratedValue
    private Integer selectionId;

    private String selectionValue;
}
