package com.yuyi.pts.common.codec;

import com.yuyi.pts.common.protocol.ModBus2Message;
import com.yuyi.pts.common.protocol.ModBusHeader;
import com.yuyi.pts.common.util.ByteBufUtils;
import com.yuyi.pts.common.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * ModBus协议解码
 *
 * @author greyson
 * @since 2021/4/30
 */
@Slf4j
public class ModBus2Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
                          List<Object> out) throws Exception {
        log.info("ModBus协议解码--begin");
        boolean DEBUG = false;
        if (DEBUG) {
            byteBuf.markReaderIndex();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            log.info("解码前收到的数据：[{}]", Arrays.toString(bytes));
            byteBuf.resetReaderIndex();
        }
        ModBus2Message modBusMessage = new ModBus2Message();
        log.debug("收到字节个数：{}", byteBuf.readableBytes());
//        ModBusMessage modBusMessage = new ModBusMessage();
        ModBusHeader modBusHeader = new ModBusHeader();
        // 协议头
        byte[] headerData = ByteBufUtils.parseByteCount(byteBuf, 7);
        modBusMessage.setHeaderBytes(headerData);
        ByteBuf headerBuffer = Unpooled.buffer();
        headerBuffer.writeBytes(headerData);
        log.debug("协议头数据：{}", Arrays.toString(headerData));

        // 业务标识符
        byte[] affairIdentification = ByteBufUtils.parseByteCount(headerBuffer, 2);
        modBusHeader.setAffairIdentification(ByteUtils.byteArrayToHexStr(affairIdentification));

        // 协议标识符
        byte[] protocolIdentification = ByteBufUtils.parseByteCount(headerBuffer, 2);
        modBusHeader.setProtocolIdentification(ByteUtils.byteArrayToHexStr(protocolIdentification));

        // 长度标识符
        byte[] lengthArr = ByteBufUtils.parseByteCount(headerBuffer, 2);
        modBusHeader.setLength(ByteUtils.byte2ToShortBigEndian(lengthArr));

        //单元标识码
        byte[] unit = ByteBufUtils.parseByteCount(headerBuffer, 1);
        modBusHeader.setUnitIdentification(ByteUtils.byteArrayToHexStr(unit));

        modBusMessage.setHeader(modBusHeader);

        // 解析消息体
        int dadaLength = ByteUtils.byte2ToShortBigEndian(lengthArr) - 1;
        int i = byteBuf.readableBytes();
        log.debug("协议解码的消息体长度是：[{}]，可读字节长度是：[{}]", dadaLength, i);
        if (dadaLength < i) {
            dadaLength = i;
        }
        byte[] data = ByteBufUtils.parseByteCount(byteBuf, dadaLength);
        modBusMessage.setBodyData(data);
        log.debug("解码数据：消息数据--{}", Arrays.toString(data));
        if (dadaLength == i) {
            out.add(modBusMessage);
            log.info("ModBus协议解码--end");
        }
    }
}
