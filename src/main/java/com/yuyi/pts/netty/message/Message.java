package com.yuyi.pts.netty.message;


import com.yuyi.pts.common.util.ByteBufUtils;
import com.yuyi.pts.common.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private MessageHeader header;
    private MessageBody body;


    public ByteBuf getBuf() {
        ByteBuf buffer = Unpooled.buffer();
        // 初始化长度
        short length = (short)(header.getUnitident().readableBytes()+body.getBuf().readableBytes());
        ByteBuf buffer2 = Unpooled.buffer();
        byte[] shortToByte2 = ByteUtils.shortToByte2(length);
        buffer2.writeBytes(shortToByte2);
        header.setLength(buffer2);
        buffer.writeBytes(ByteBufUtils.getByteBuf(header.getBuf()));
        buffer.writeBytes(ByteBufUtils.getByteBuf(body.getBuf()));
        return buffer;
    }

}
