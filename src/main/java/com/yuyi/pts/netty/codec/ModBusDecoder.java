package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.Desc;
import com.yuyi.pts.model.protocol.ModBusMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/30
 */
@Slf4j
@Desc("ModBus协议解码器")
public class ModBusDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
                          List<Object> out) throws Exception {
        log.info("ModBus协议解码--Begin");
        ModBusMessage modBusMessage = new ModBusMessage();
        int headlength = 7;
        int bodylength = byteBuf.readableBytes()-7;
        byte[] head = new byte[headlength];
        byte[] body= new byte[bodylength];
        byteBuf.readBytes(head);
        byteBuf.readBytes(body);

        // 业务标识符
        byte[] bussnessident = new byte[2];
        bussnessident[0]=head[0];
        bussnessident[1]=head[1];
        modBusMessage.setAffairIdentification(ByteUtils.byteArrayToHexStr(bussnessident));

        // 协议标识符
        byte[] protocalident = new byte[2];
        protocalident[0]=head[2];
        protocalident[1]=head[3];
        modBusMessage.setProtocolIdentification(ByteUtils.byteArrayToHexStr(protocalident));

        // 长度标识符
        byte[] lengthArr = new byte[2];
        lengthArr[0]=head[4];
        lengthArr[1]=head[5];
        modBusMessage.setLength(ByteUtils.byteArrayToHexStr(lengthArr));

        //单元标识码
        byte[] unit = new byte[1];
        unit[0]=head[6];
        modBusMessage.setUnitIdentification(ByteUtils.byteArrayToHexStr(unit));

        //功能码
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(body[0]);
        if(!"04".equals(ByteBufUtil.hexDump(buffer))&&!"10".equals(ByteBufUtil.hexDump(buffer))){
            buffer.writeByte(body[1]);
        }
        modBusMessage.setCode(ByteBufUtil.hexDump(buffer));

        // 数据
        byte[] data = null ;
        int length;
        if(!"04".equals(ByteBufUtil.hexDump(buffer))&&!"10".equals(ByteBufUtil.hexDump(buffer))){
            data = new byte[bodylength-2];
            length=bodylength-2;
        }else {
            data = new byte[bodylength-1];
            length=bodylength-1;
        }
        for(int i=0;i<length;i++){
            data[i]=body[i+1];
        }

        modBusMessage.setBody(ByteUtils.byteArrayToHexStr(data));

        out.add(modBusMessage);
        log.info("ModBus协议解码--End");

    }
}
