package com.yuyi.pts.common.codec;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.protocol.ModBus2Message;
import com.yuyi.pts.common.protocol.ModBusBody;
import com.yuyi.pts.common.protocol.ModBusHeader;
import com.yuyi.pts.common.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/21
 */
@Slf4j
@Component
public class ModBus2Encoder extends MessageToByteEncoder<ModBus2Message> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ModBus2Message modBusMessage, ByteBuf out) throws Exception {
        log.info("ModBus协议编码--begin");

        ModBusHeader header = modBusMessage.getHeader();
        // 业务标识符 两个字节
        byte[] affairIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        header.getAffairIdentification()), 2);
        // 协议标识符 两个字节
        byte[] protocolIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        header.getProtocolIdentification()), 2);
        //  单元标识码  一个字节
        byte[] unitIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(header.getUnitIdentification()), 1);


        // 写出消息头
        out.writeBytes(affairIdentification);
        out.writeBytes(protocolIdentification);


        // 写消息体 isWriteBody-- 是否需要写入ModBusBody
        Boolean isWriteBody = true;
        if (modBusMessage.getBodyData() != null) {
            //  长度两个字节 == 单元标识符一个字节 + 数据长度
            int length = modBusMessage.getBodyData().length + 1;
            byte[] lengthByte = ByteUtils.shortToByte2((short) length);
            out.writeBytes(lengthByte);
            out.writeBytes(unitIdentification);
            out.writeBytes(modBusMessage.getBodyData());
            isWriteBody = false;
        } else if (modBusMessage.getBody() != null && isWriteBody) {
            ModBusBody body = modBusMessage.getBody();
            String functionCode = body.getFunctionCode();
            // 标识
            Boolean flag = false;
            byte[] code = ByteUtils.storeInBytes(
                    ByteUtils.hexString2Bytes(functionCode), 1);
            byte[] startAddress = null;
            byte[] registerCount = null;
            byte[] data = null;
            int byteCount = 0;
            // 单元标识符 + 功能码
            int length = 2;
            // 04请求数据和10响应数据编码一致，flag设为true
            if (ConstantValue.MODBUS_READ.equals(functionCode) && !body.getIsResponse()) {
                flag = true;
            }
            if (ConstantValue.MODBUS_WRITE.equals(functionCode) && body.getIsResponse()) {
                flag = true;
            }

            if (flag) {
                startAddress = ByteUtils.shortToByte2(body.getStartAddress());
                registerCount = ByteUtils.shortToByte2(body.getRegisterCounters());
                length += startAddress.length + registerCount.length;
            } else {
                // 10 请求独有部分
                if (ConstantValue.MODBUS_WRITE.equals(functionCode)) {
                    startAddress = ByteUtils.shortToByte2(body.getStartAddress());
                    registerCount = ByteUtils.shortToByte2(body.getRegisterCounters());
                    length += startAddress.length + registerCount.length;
                }
                // 04 响应和 10 请求 公共部分
                byteCount = body.getByteCount();
                length += 1;
                data = body.getData();
                length += body.getData().length;
            }

            out.writeBytes(ByteUtils.shortToByte2((short) length));
            out.writeBytes(unitIdentification);
            out.writeBytes(code);
            if (startAddress != null) {
                out.writeBytes(startAddress);
            }
            if (registerCount != null) {
                out.writeBytes(registerCount);
            }
            if (data != null) {
                out.writeByte(byteCount);
                out.writeBytes(data);
            }

        }
        log.info("ModBus协议编码--end");
        boolean DEBUG = false;
        if (DEBUG) {
            ByteBuf copy = out.copy();
            byte[] bytes = new byte[copy.readableBytes()];
            copy.readBytes(bytes);
            log.info("编码完成的数据：[{}]", Arrays.toString(bytes));
        }
    }
}

