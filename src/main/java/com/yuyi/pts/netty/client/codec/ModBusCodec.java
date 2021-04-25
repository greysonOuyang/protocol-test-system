package com.yuyi.pts.netty.client.codec;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.ModBusMessage;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ModBus协议编码解码器
 *
 * @author greyson/wzl
 * @since 2021/4/16
 */
@Slf4j
@Component
public class ModBusCodec extends ByteToMessageCodec<RequestDataDto> {


    private static ModBusMessage modBusMessage;
    static {
        modBusMessage = SpringUtils.getBean(ModBusMessage.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("modbuscodec激活了");
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
        byte[] strAddressBytes = ByteUtils.strToBytes(startAddress);
        String registerCount = (String) jsonObject.get("registerCount");
        byte[] registerCountBytes = ByteUtils.strToBytes(registerCount);
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

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
                          List<Object> out) throws Exception {
        log.info("ModBus协议接收到服务端数据--解码前：{}", byteBuf);
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
        byte[] code = new byte[1];
        code[0] = body[0];
        modBusMessage.setCode(ByteUtils.byteArrayToHexStr(code));

        // 数据
        byte[] data = new byte[bodylength-1];
        for(int i=0;i<bodylength-1;i++){
            data[i]=body[i+1];
        }

        modBusMessage.setBody(ByteUtils.byteArrayToHexStr(data));

        out.add(modBusMessage);
    }

}
