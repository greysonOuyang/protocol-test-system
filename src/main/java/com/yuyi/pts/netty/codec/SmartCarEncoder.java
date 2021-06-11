package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.AtsMessage;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Ats信号编码、解码
 *
 * @author greyson
 * @since 2021/4/25
 */
@Slf4j
@Component
public class SmartCarEncoder extends MessageToByteEncoder<Object> {

    static AtsMessage atsMessage;


    static {
        atsMessage = SpringUtils.getBean(AtsMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        log.info("进入了Ats编码");
        byte[] sourceByteArr = (byte[]) obj;

        byte[] dataHead = ByteUtils.shortToByte2(atsMessage.getDataHead());
        byteBuf.writeBytes(dataHead);
        byteBuf.writeByte(atsMessage.getTotal());
        byteBuf.writeByte(atsMessage.getIndex());
        int size = sourceByteArr.length + 2;
        byte[] dataLen = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(size), 2);
        byteBuf.writeBytes(dataLen);
        byte[] deviceStatus = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(atsMessage.getDeviceStatus()), 1);
        byteBuf.writeBytes(deviceStatus);
        byte [] type = ByteUtils.hexString2Bytes(atsMessage.getType());
        byteBuf.writeBytes(type);
  //      System.arraycopy(1, 2, content, 0,requestDataDto.getBody().toString().length());
        byteBuf.writeBytes(sourceByteArr);
        byte[] dataTail = ByteUtils.shortToByte2(
                atsMessage.getHeadTail());
        byteBuf.writeBytes(dataTail);

    }

}
