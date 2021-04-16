package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.service.ProcessRequestService;
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

    @Override
    public void sendBinMessage(ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        Object body = dataContent.getBody();
        byte[] bytes = SerializeUtil.serialize(body);
        log.info("客户端往服务端发送的数据：" + body);
        currentCtx.writeAndFlush(bytes);
    }
}
