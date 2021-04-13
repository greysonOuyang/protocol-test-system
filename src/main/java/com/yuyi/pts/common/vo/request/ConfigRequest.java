package com.yuyi.pts.common.vo.request;
/**
 * @author : wzl
 * @date   : 2021/4/12/16:29
 * @description:
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfigRequest {
    @JsonProperty("printResInfo")
    private Boolean printResInfo;
    @JsonProperty("keepAlive")
    private Boolean keepAlive;
    @JsonProperty("poolSize")
    private Object poolSize;
    @JsonProperty("timeout")
    private Object timeout;
}
