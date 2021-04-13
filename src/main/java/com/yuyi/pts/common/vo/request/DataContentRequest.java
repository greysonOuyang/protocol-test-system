package com.yuyi.pts.common.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 未确定是否启用模型，编码时再定，或许直接用FASTJSON
 *
 * @author greyson
 * @since 2021/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataContentRequest {

    @JsonProperty("isStatistics")
    private Boolean isStatistics;
    @JsonProperty("statisticsInfo")
    private String statisticsInfo;
    @JsonProperty("isExecuting")
    private Boolean isExecuting;
    @JsonProperty("isExecuted")
    private Boolean isExecuted;
    @JsonProperty("printShowLimit")
    private Integer printShowLimit;
    @JsonProperty("requestConfig")
    private ConfigRequest requestConfig;
    @JsonProperty("requestData")
    private DataRequest requestData;
    @JsonProperty("requestDataLen")
    private Integer requestDataLen;
    @JsonProperty("responseDataLen")
    private Integer responseDataLen;
    @JsonProperty("responseSucceeded")
    private Integer responseSucceeded;
    @JsonProperty("responseFailed")
    private Integer responseFailed;
    @JsonProperty("responseTimeCount")
    private List<?> responseTimeCount;
    @JsonProperty("serverMetrics")
    private ServerMetricsRequest serverMetrics;
    @JsonProperty("websocket")
    private Object websocket;
    @JsonProperty("consoleInfos")
    private List<?> consoleInfos;

}