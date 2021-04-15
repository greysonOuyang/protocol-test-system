package com.yuyi.pts.service;

import com.yuyi.pts.common.vo.request.RequestDataDto;
import io.netty.channel.ChannelHandlerContext;

/**
 * 向第三方接口发送消息的服务
 *
 * @author greyson
 * @since 2021/4/15
 */
public interface ProcessRequestService {

    void sendBinMessage(ChannelHandlerContext currentCtx, RequestDataDto dataContent);

}
