package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.AtsMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        byte[] sourceByteArr = (byte[]) obj;
        byte[] messageType = new byte[2];
        messageType[0] = sourceByteArr[0];
        messageType[1] = sourceByteArr[1];
        log.info("进入了Ats编码");
        byte[] dataHead = ByteUtils.shortToByte2(atsMessage.getDataHead());
        byteBuf.writeBytes(dataHead);
        byteBuf.writeByte(atsMessage.getTotal());
        byteBuf.writeByte(atsMessage.getIndex());
        int size = sourceByteArr.length + 2;
        byte[] dataLen = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(size), 2);
        byteBuf.writeBytes(dataLen);
        byte[] deviceStatus = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(atsMessage.getDeviceStatus()), 1);
        byteBuf.writeBytes(deviceStatus);
        byteBuf.writeBytes(messageType);
        byte[] content = new byte[sourceByteArr.length - 2];
        System.arraycopy(sourceByteArr, 2, content, 0, sourceByteArr.length - 2);
        byteBuf.writeBytes(content);
        byte[] dataTail = ByteUtils.shortToByte2(
                atsMessage.getHeadTail());
        byteBuf.writeBytes(dataTail);

    }

}
