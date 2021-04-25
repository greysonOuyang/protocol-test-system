package com.yuyi.pts.netty.client.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.AtsMessage;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ats信号编码、解码
 *
 * @author greyson
 * @since 2021/4/25
 */
@Slf4j
@Component
public class AtsCodec extends ByteToMessageCodec<RequestDataDto> {

    static AtsMessage atsMessage;

    static {
        atsMessage = SpringUtils.getBean(AtsMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestDataDto requestDataDto, ByteBuf byteBuf) throws Exception {
        log.info("进入了Ats编码");
        byte[] dataHead = ByteUtils.shortToByte2(
                atsMessage.getDataHead());

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
