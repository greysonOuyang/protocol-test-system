package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.service.ProcessRequestService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/15
 */
@Slf4j
@Service
public class ProcessRequestServiceImpl implements ProcessRequestService {


    /**
     * 往第三方接口系统发送二进制数据、即序列化后的结果
     *
     * @param currentCtx
     * @param dataContent
     */
    @Override
    public void sendBinMessage(ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        byte[] serialize = SerializeUtil.serialize(dataContent);
        ByteBuf buffer = currentCtx.alloc().buffer();
        buffer.writeBytes(serialize);
        log.info("客户端往服务端发送的数据：" + dataContent);
        currentCtx.writeAndFlush(buffer);
    }
}
