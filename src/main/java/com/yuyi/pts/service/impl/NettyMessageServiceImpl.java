package com.yuyi.pts.service.impl;
/*
 * @author : wzl
 * @date   : 2021/4/12/16:59
 * @description:  处理前端发来的信息，进行数据封装
 */

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.vo.request.DataRequest;
import com.yuyi.pts.netty.client.NettyClient1;
import com.yuyi.pts.service.NettyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NettyMessageServiceImpl implements NettyMessageService {

    NettyClient1 nettyClient = new NettyClient1();
    @Autowired
    private NettyClient1 nettyClient1;
    public JSONObject getConnect(String message){
        JSONObject object = JSONObject.parseObject(message);
        DataRequest dataRequest = new DataRequest();
        if(null!=object){
            // 获取到前端上送的ip、端口、类型、具体内容
            dataRequest.setHost(object.get("ip").toString());
        }
        // 将IP 端口获取到
        nettyClient.setHost(object.get("ip").toString());
        nettyClient.setPort(Integer.parseInt(object.get("port").toString()));
        // 在这启动netty客户端，调用第三方接口服务
        //nettyClient.start(object.get("type").toString());
        nettyClient.start("tcp");
        return object;
    }
}
