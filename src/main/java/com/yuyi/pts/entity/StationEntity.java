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
 * @since 2021/9/25
 */
@Entity
@Table(name = "t_station_info")
@Data
public class StationEntity {
    @Id
    @GeneratedValue
    private Integer Id;

    private String stationId;

    private String stationName;

    private String ip;

    private String password;
    
}
