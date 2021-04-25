package com.yuyi.pts.model.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.net.httpserver.Headers;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.enums.SslCertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 前后端通信模型，为RequestMainData.data
 *
 * @author greyson
 * @since 2021/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDataDto {


    /** UUID生成，唯一ID，每一次请求的标识 */
    @JsonProperty("id")
    private String id;

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

    @JsonProperty("printResInfo")
    private Boolean printResInfo;
    @JsonProperty("keepAlive")
    private Boolean keepAlive;
    @JsonProperty("poolSize")
    private Object poolSize;
    @JsonProperty("timeout")
    private Object timeout;

    @JsonProperty("requestType")
    private RequestType type;
    @JsonProperty("webSocketVersion")
    private String webSocketVersion;

    @JsonProperty("subProtocols")
    private List<String> subProtocols;

    @JsonProperty("serverName")
    private String serverName;

    @JsonProperty("isSSL")
    private Boolean isSSL;

    @JsonProperty("cert")
    private SslCertType cert;

    @JsonProperty("certKey")
    private Object certKey;
    @JsonProperty("certValue")
    private Object certValue;
    @JsonProperty("host")
    private String host;
    @JsonProperty("port")
    private Integer port;
    @JsonProperty("method")
    private HttpMethod method;
    @JsonProperty("headers")
    private Headers headers;
    @JsonProperty("url")
    private String url;
    @JsonProperty("body")
    private Object body;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("average")
    private Integer average;
    @JsonProperty("interval")
    private Long interval;

    /**
     * 应用层协议
     */
    private RequestType.ProtocolType protocolType;

}
