//package com.yuyi.pts.model;
//
//import com.sun.net.httpserver.Headers;
//import com.yuyi.pts.common.enums.SslCertType;
//import com.yuyi.pts.common.enums.RequestType;
//import io.netty.buffer.ByteBuf;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.http.HttpMethod;
//
//import java.util.List;
//
///**
// * 请求配置
// *
// * @author <a href="http://mirrentools.org">Mirren</a>
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class RequestDataDto {
//    /**
//     * 当前请求配置的id通常对应客户端WebSocket的写id
//     */
//    private String id;
//    /**
//     * 请求的类型
//     */
//    private RequestType type;
//    /**
//     * 主机地址,仅TCP有效
//     */
//    private String host;
//    /**
//     * 端口号,仅TCP有效
//     */
//    private int port;
//    /**
//     * 服务器的名称,仅TCP有效
//     */
//    private String serverName;
//    /**
//     * WebSocket的版本
//     */
//    private String webSocketVersion;
//    /**
//     * WenSocket的请求子协议
//     */
//    private List<String> subProtocols;
//    /**
//     * 请求的url
//     */
//    private String url;
//    /**
//     * http请求的method类型
//     */
//    private HttpMethod method;
//    /**
//     * 是否使用SSL
//     */
//    private boolean ssl;
//    /**
//     * 证书的类型
//     */
//    private SslCertType cert;
//    /**
//     * 证书的key
//     */
//    private String certKey;
//    /**
//     * 证书的value
//     */
//    private String certValue;
//    /**
//     * 请求的header数据
//     */
//    private Headers headers;
//    /**
//     * 请求的body
//     */
//    private ByteBuf body;
//    /**
//     * 请求的总次数
//     */
//    private int count;
//    /**
//     * 每次请求多数次
//     */
//    private int average;
//    /**
//     * 请求的间隔
//     */
//    private long interval;
//    /**
//     * 是否输出URL服务器返回的数据
//     */
//    private boolean printResInfo;
//    /**
//     * 是否保持连接
//     */
//    private boolean keepAlive;
//    /**
//     * 最大建立连接数
//     */
//    private int poolSize;
//    /**
//     * 请求超时时间(ms)
//     */
//    private Integer timeout;
//
//
//}
