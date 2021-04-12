package com.yuyi.pts.common.vo.request;/*
 * @author : wzl
 * @date   : 2021/4/12/16:29
 * @description:
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerMetricsRequest {
    @JsonProperty("processors")
    private Integer processors;
    @JsonProperty("totalMemory")
    private Integer totalMemory;
    @JsonProperty("maxMemory")
    private Integer maxMemory;
    @JsonProperty("freeMemory")
    private Integer freeMemory;
}
