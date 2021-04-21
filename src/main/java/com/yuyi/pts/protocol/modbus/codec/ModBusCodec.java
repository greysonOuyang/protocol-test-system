package com.yuyi.pts.protocol.modbus.codec;

import com.yuyi.pts.common.util.ByteBufUtils;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.protocol.modbus.model.ModBusMessage;
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

    /**
     * <pre>
     * data_head为帧头；两个字节，固定为0xEF,0xEF
     * Total:总帧数；1个字节
     * Index:当前帧，1个字节；
     * data_len 表示后接的应用数据（data部分）的长度；两个字节，低字节在前，高字节在后
     * Dev_Status表示设备当前主/备机状态，0x01:当前为主机，0x02:当前为备机1个字节；
     * </pre>
     */
    private static final int HEAD_LENGTH = 7;

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
//        ModBusMessage modBusMessage = new ModBusMessage();
//        modBusMessage = requestDataDto.getModBusMessage();
        // 业务标识符 两个字节
        byte[] affairIdentification = new byte[2];
        affairIdentification = ByteBufUtils.hexString2Bytes(modBusMessage.getAffairIdentification());
        // 协议标识符 两个字节
        byte[] protocolIdentification = new byte[2];
        protocolIdentification = ByteBufUtils.hexString2Bytes(modBusMessage.getProtocolIdentification());
        //  单元标识码  一个字节
        byte[] unitIdentification = new byte[1];
         unitIdentification = ByteBufUtils.hexString2Bytes(modBusMessage.getUnitIdentification());
        //  功能码 一个字节
        byte[] code = new byte[1];
         code = ByteBufUtils.hexString2Bytes(modBusMessage.getCode());
        //  写入数据
        Object body = requestDataDto.getBody();
        byte[] data = SerializeUtil.serialize(body);
        //  长度两个字节 == 单元标识符一个字节 + 数据长度
        int length = data.length + 1;


        // 注意！写入顺序不可调整
        out.writeBytes(affairIdentification);
        out.writeBytes(protocolIdentification);
        out.writeShort(length);
        out.writeBytes(unitIdentification);
        out.writeBytes(code);
        out.writeBytes(data);

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
        modBusMessage.setCode(ByteUtils.byteArrayToStr(code));

        // 数据
        byte[] data = new byte[bodylength-1];
        for(int i=0;i<bodylength-1;i++){
            data[i]=body[i+1];
        }

        modBusMessage.setBody(ByteUtils.byteArrayToStr(data));

        out.add(modBusMessage);
    }

}
