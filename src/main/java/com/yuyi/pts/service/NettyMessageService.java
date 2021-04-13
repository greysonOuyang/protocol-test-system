package com.yuyi.pts.service;
/*
 * @author : wzl
 * @date   : 2021/4/12/16:59
 * @description:  处理前端发来的信息，进行数据封装,建立连接第三方系统
 */

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.vo.request.DataRequest;
import com.yuyi.pts.netty.client.NettyClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public interface NettyMessageService {

    public JSONObject getConnect(String message);
}
