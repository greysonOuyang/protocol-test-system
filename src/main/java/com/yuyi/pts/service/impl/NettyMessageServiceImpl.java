package com.yuyi.pts.service.impl;
/*
 * @author : wzl
 * @date   : 2021/4/12/16:59
 * @description:  处理前端发来的信息，进行数据封装
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ApplicationHelper;
import com.yuyi.pts.common.vo.request.DataRequest;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.common.vo.request.RequestMainDTO;
import com.yuyi.pts.netty.client.NettyClient1;
import com.yuyi.pts.service.NettyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NettyMessageServiceImpl implements NettyMessageService {


    private NettyClient1 nettyClient1 = ApplicationHelper.getBean("nettyClient1");
    public JSONObject getConnect(String message){
        JSONObject object = JSONObject.parseObject(message);
        RequestMainDTO request = JSON.toJavaObject(object, RequestMainDTO.class);
        int code = request.getCode();
        RequestDataDto data = request.getData();
        // 将IP 端口获取到
        nettyClient1.setHost(data.getHost());
        nettyClient1.setPort(data.getPort());
        // 在这启动netty客户端，调用第三方接口服务
        nettyClient1.start(data.getType().toString(),data);
        return object;
    }
}
