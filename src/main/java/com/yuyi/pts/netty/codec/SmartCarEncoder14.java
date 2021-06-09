package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.server.SmartCarProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/2
 */
@Slf4j
@Component
public class SmartCarEncoder14 extends MessageToByteEncoder<Object> {

    static SmartCarProtocol protocol;

    static {
        protocol = SpringUtils.getBean(SmartCarProtocol.class);
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        log.info("进入了Ats编码");
        byte[] sourceByteArr = (byte[]) obj;
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(sourceByteArr);
        byte[] dataHead = ByteUtils.shortToByte2SmallEnd(protocol.getFrameHead());
        byteBuf.writeBytes(dataHead);
        // 信息正文长度
        int length = sourceByteArr.length;
        byte[] frameCount = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(protocol.getFrameCount()), 1);
        byteBuf.writeBytes(frameCount);
        byte[] frameIndex = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(protocol.getFrameIndex()), 1);
        byteBuf.writeBytes(frameIndex);
        byte[] dataLen = ByteUtils.shortToByte2SmallEnd(ByteUtils.intToShort(length + 31));
        byteBuf.writeBytes(dataLen);
        // messageLen = content长度 + Message_Time(7) + Line_Id(2) + Spare(18) + Message_Id (1) + version (1)
        byte[] messageLen = ByteUtils.shortToByte2SmallEnd(ByteUtils.intToShort(length + 29));
        byteBuf.writeBytes(messageLen);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String format = sdf.format(timestamp);
        String[] split = format.split("\\.");
        byte[] year = ByteUtils.shortToByte2SmallEnd(ByteUtils.intToShort(Integer.parseInt(split[0])));
        byte[] month = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(split[1])), 1);
        byte[] day = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(split[2])), 1);
        byte[] hour = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(split[3])), 1);
        byte[] min = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(split[4])), 1);
        byte[] second = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(split[5])), 1);
        byteBuf.writeBytes(year);
        byteBuf.writeBytes(month);
        byteBuf.writeBytes(day);
        byteBuf.writeBytes(hour);
        byteBuf.writeBytes(min);
        byteBuf.writeBytes(second);
        byte[] lineId = ByteUtils.shortToByte2SmallEnd(ByteUtils.intToShort(protocol.getLineId()));
        byteBuf.writeBytes(lineId);
        // 预留字段
        byte[] spare = new byte[18];
        Arrays.fill(spare, (byte) 0xEF);
        byteBuf.writeBytes(spare);
   //     byte[] messageId = buf.readShort();
        byteBuf.writeByte(buf.readShort());
        byte[] version = ByteUtils.hexString2Bytes(protocol.getVersion());
        byteBuf.writeBytes(version);
        byte[] content = sourceByteArr;
        byteBuf.writeBytes(content);
        byte[] frameTail = ByteUtils.shortToByte2SmallEnd(
                protocol.getFrameTail());
        byteBuf.writeBytes(frameTail);
    }

}
