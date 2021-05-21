package com.yuyi.pts.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/5/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInterface {
    private String id;
    private String requestName;
    private String requestType;
    private String url;
    private String requestMethod;
    private String content;
    private int port;
    private List<Config> configList;

}
