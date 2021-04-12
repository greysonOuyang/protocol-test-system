package com.yuyi.pts.common.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@NoArgsConstructor
@Data
public class ServerMetricsDTO {
    @JsonProperty("processors")
    private Integer processors;
    @JsonProperty("totalMemory")
    private Integer totalMemory;
    @JsonProperty("maxMemory")
    private Integer maxMemory;
    @JsonProperty("freeMemory")
    private Integer freeMemory;
}
