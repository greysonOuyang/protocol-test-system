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
     * 往第三方接口系统发送二进制数据、即序列化后的结果 只处理发送数的数据
     *
     * @param currentCtx
     * @param dataContent
     */
    void sendBinMessage(RequestType type,ChannelHandlerContext currentCtx, RequestDataDto dataContent);

}
