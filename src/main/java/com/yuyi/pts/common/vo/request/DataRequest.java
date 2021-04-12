package com.yuyi.pts.common.vo.request;/*
 * @author : wzl
 * @date   : 2021/4/12/16:29
 * @description:
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataRequest {
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
