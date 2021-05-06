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
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf outBuf) throws Exception {
        ByteBuf bodyBuffer = (ByteBuf) obj;
        log.info("进入了Ats编码");
        byte[] dataHead = ByteUtils.shortToByte2(atsMessage.getDataHead());
        outBuf.writeBytes(dataHead);
        outBuf.writeByte(atsMessage.getTotal());
        outBuf.writeByte(atsMessage.getIndex());
        int dataLen = bodyBuffer.readableBytes() + 2;
        outBuf.writeBytes(ByteUtils.storeInBytesLow(ByteUtils.intToByte4(dataLen), 2));
        byte[] deviceStatus = ByteUtils.storeInBytesLow(ByteUtils.convertHEXString2ByteArray(atsMessage.getDeviceStatus()), 1);
        outBuf.writeBytes(deviceStatus);
        byte[] type = ByteUtils.storeInBytesLow(ByteUtils.convertHEXString2ByteArray(atsMessage.getType()), 1);
        outBuf.writeBytes(type);
        byte[] dataTail = ByteUtils.shortToByte2(
                atsMessage.getHeadTail());
        byte[] body = new byte[bodyBuffer.readableBytes()];
        bodyBuffer.readBytes(body);
        outBuf.writeBytes(body);
        outBuf.writeBytes(dataTail);
    }

}
