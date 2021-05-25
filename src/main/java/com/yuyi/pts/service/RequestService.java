package com.yuyi.pts.service;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.netty.channel.ChannelHandlerContext;

/**
 * 向第三方接口发送消息的服务
 *
 * @author greyson
 * @since 2021/4/15
 */
public interface RequestService {

    /**
     * 往第三方接口系统发送文本
     *
     * @param currentCtx 当前handler处理器上下文
     * @param dataContent 原始数据
     */
    void sendTextMessage(RequestType type, ChannelHandlerContext currentCtx, RequestDataDto dataContent);

}
