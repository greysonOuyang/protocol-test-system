package com.yuyi.pts.netty.codec;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.ModBusMessage;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ModBus协议编码解码器
 *
 * @author greyson/wzl
 * @since 2021/4/16
 */
@Slf4j
@Component
public class ModBusEncoder extends MessageToByteEncoder<RequestDataDto> {


    private static ModBusMessage modBusMessage;
    static {
        modBusMessage = SpringUtils.getBean(ModBusMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestDataDto requestDataDto, ByteBuf out) throws Exception {
        log.info("进入了modBus编码");
        // 业务标识符 两个字节 TODO 批量时考虑生成自增顺序号
        byte[] affairIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        modBusMessage.getAffairIdentification()), 2);
        // 协议标识符 两个字节
        byte[] protocolIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        modBusMessage.getProtocolIdentification()), 2);
        //  单元标识码  一个字节
        byte[] unitIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(modBusMessage.getUnitIdentification()), 1);

        // TODO 长度包含哪些值待确定 发送数据方式：  1.直接发送body  2. 构造后再发送 现在选择的方式是2 对数据有严格要求，对1的支持后续再处理
        Object body = requestDataDto.getBody();
        String jsonString = JSONObject.toJSONString(body);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //  功能码 一个字节
        String functionCode = (String) jsonObject.get("functionCode");
        functionCode = functionCode == null ? modBusMessage.getCode() : functionCode;
        byte[] code = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(functionCode), 1);
        //  写入数据
       String startAddress = (String) jsonObject.get("startAddress");
        byte[] strAddressBytes = ByteUtils.hexString2Bytes(startAddress);
        String registerCount = (String) jsonObject.get("registerCount");
        byte[] registerCountBytes = ByteUtils.hexString2Bytes(registerCount);
        byte[]  bytes = new byte[2];
        bytes[0] = registerCountBytes[0] ;
        //  长度两个字节 == 单元标识符一个字节 + 数据长度
        int length = code.length + strAddressBytes.length + registerCountBytes.length + 1;
        byte[] lengthBytes = ByteUtils.storeInBytes(
                ByteUtils.intToBytesLow(length), 2);
        // 注意！写入顺序不可调整
        out.writeBytes(affairIdentification);
        out.writeBytes(protocolIdentification);
        out.writeBytes(lengthBytes);
        out.writeBytes(unitIdentification);
        out.writeBytes(code);
        out.writeBytes(strAddressBytes);
        out.writeBytes(registerCountBytes);

    }

}
