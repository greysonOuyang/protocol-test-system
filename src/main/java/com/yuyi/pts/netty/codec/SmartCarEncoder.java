package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.AtsMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
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
public class SmartCarEncoder extends MessageToByteEncoder<Object> {

    static AtsMessage atsMessage;

    static {
        atsMessage = SpringUtils.getBean(AtsMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        List<byte[]> byteList = (List<byte[]>) obj;
        log.info("进入了Ats编码");
        byte[] dataHead = ByteUtils.shortToByte2(
                atsMessage.getDataHead());
        byteBuf.writeBytes(dataHead);
        byteBuf.writeByte(atsMessage.getTotal());
        byteBuf.writeByte(atsMessage.getIndex());
        int size = byteList.size() + 2;
        byte[] dataLen = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(size), 2);
        byteBuf.writeBytes(dataLen);
        byte[] deviceStatus = ByteUtils.convertHEXString2ByteArray(atsMessage.getDeviceStatus());
        byteBuf.writeBytes(deviceStatus);
        byte[] type = ByteUtils.storeInBytes(ByteUtils.convertHEXString2ByteArray(atsMessage.getType()), 2);
        byteBuf.writeBytes(type);
        for (byte[] bytes : byteList) {
            byteBuf.writeBytes(bytes);
        }
        byte[] dataTail = ByteUtils.shortToByte2(
                atsMessage.getHeadTail());
        byteBuf.writeBytes(dataTail);

    }

}
