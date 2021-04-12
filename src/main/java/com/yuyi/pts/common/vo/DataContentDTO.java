package com.yuyi.pts.common.vo;

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
public class DataContentDTO {

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
    private RequestConfigDTO requestConfig;
    @JsonProperty("requestData")
    private RequestDataDTO requestData;
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
    private ServerMetricsDTO serverMetrics;
    @JsonProperty("websocket")
    private Object websocket;
    @JsonProperty("consoleInfos")
    private List<?> consoleInfos;

    @NoArgsConstructor
    @Data
    public static class RequestConfigDTO {
        @JsonProperty("printResInfo")
        private Boolean printResInfo;
        @JsonProperty("keepAlive")
        private Boolean keepAlive;
        @JsonProperty("poolSize")
        private Object poolSize;
        @JsonProperty("timeout")
        private Object timeout;
    }

    @NoArgsConstructor
    @Data
    public static class RequestDataDTO {
        @JsonProperty("requestType")
        private String requestType;
        @JsonProperty("webSocketVersion")
        private String webSocketVersion;
        @JsonProperty("subProtocols")
        private List<?> subProtocols;
        @JsonProperty("serverName")
        private String serverName;
        @JsonProperty("isSSL")
        private Boolean isSSL;
        @JsonProperty("cert")
        private String cert;
        @JsonProperty("certKey")
        private Object certKey;
        @JsonProperty("certValue")
        private Object certValue;
        @JsonProperty("host")
        private String host;
        @JsonProperty("port")
        private Object port;
        @JsonProperty("method")
        private String method;
        @JsonProperty("headers")
        private List<?> headers;
        @JsonProperty("url")
        private String url;
        @JsonProperty("body")
        private Object body;
        @JsonProperty("count")
        private Object count;
        @JsonProperty("average")
        private Object average;
        @JsonProperty("interval")
        private Object interval;
    }

    @NoArgsConstructor
    @Data
    public static class ServerMetricsDTO {
        @JsonProperty("processors")
        private Integer processors;
        @JsonProperty("totalMemory")
        private Integer totalMemory;
        @JsonProperty("maxMemory")
        private Integer maxMemory;
        @JsonProperty("freeMemory")
        private Integer freeMemory;
    }
}
