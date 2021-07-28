package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.Desc;
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
@Desc(value = "Ats信号编码")
public class SmartCarEncoder extends MessageToByteEncoder<Object> {

    static AtsMessage atsMessage;


    static {
        atsMessage = SpringUtils.getBean(AtsMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        log.info("ATS编码--Begin");
        byte[] sourceByteArr = (byte[]) obj;
        byte[] dataHead = ByteUtils.shortToByte2(atsMessage.getDataHead());
        byteBuf.writeBytes(dataHead);
        byteBuf.writeByte(atsMessage.getTotal());
        byteBuf.writeByte(atsMessage.getIndex());
        // sourceByteArr.length包含了消息类型即type一个字节，但type传输过去需要两个字节，所以此处加一
        int size = sourceByteArr.length + 1;
        byte[] dataLen = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(size), 2);
        byteBuf.writeBytes(dataLen);
        byte[] deviceStatus = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(atsMessage.getDeviceStatus()), 1);
        byteBuf.writeBytes(deviceStatus);
        byte[] type = new byte[2];
        type[0] = sourceByteArr[0];
        byteBuf.writeBytes(type);
        byte[] content = new byte[sourceByteArr.length-1];
        System.arraycopy(sourceByteArr, 1, content, 0, sourceByteArr.length-1);
        byteBuf.writeBytes(content);
        byte[] dataTail = ByteUtils.shortToByte2(
                atsMessage.getHeadTail());
        byteBuf.writeBytes(dataTail);
        log.info("ATS编码--End");
    }

}
