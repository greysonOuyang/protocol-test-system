package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.netty.client.NettyClient;
import com.yuyi.pts.service.TcpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.www.http.HttpClient;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/12
 */
@Service
@Slf4j
public class TcpRequestServiceImpl implements TcpRequestService {

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void submitRequest(String host, Integer port, RequestType type) {
        nettyClient.setHost(host);
        nettyClient.setPort(port);
        nettyClient.start(type);
    }
}
