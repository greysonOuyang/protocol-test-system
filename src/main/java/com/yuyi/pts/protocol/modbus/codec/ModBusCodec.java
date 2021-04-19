package com.yuyi.pts.protocol.modbus.codec;

import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.protocol.modbus.model.ModBusMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ModBus协议编码解码器
 *
 * @author greyson/wzl
 * @since 2021/4/16
 */
@Slf4j
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("modbuscodec激活了");
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestDataDto requestDataDto, ByteBuf out) throws Exception {
        log.info("进入了modBus编码");
        ModBusMessage modBusMessage = new ModBusMessage();
//        modBusMessage = requestDataDto.getModBusMessage();
        // 业务标识符 两个字节
        byte[] affairIdentification = new byte[2];
        affairIdentification = SerializeUtil.serialize(modBusMessage.getAffairIdentification());
        // 协议标识符 两个字节
        byte[] protocolIdentification = new byte[2];
        protocolIdentification = SerializeUtil.serialize(modBusMessage.getProtocolIdentification());
        //  单元标识码  一个字节
        byte[] unitIdentification = new byte[1];
         unitIdentification = SerializeUtil.serialize(modBusMessage.getUnitIdentification());
        //  功能码 一个字节
        byte[] code = new byte[1];
         code = SerializeUtil.serialize(modBusMessage.getCode());
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
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        log.info("ModBus协议接收到服务端数据--解码前：{}", in);
    }

}
