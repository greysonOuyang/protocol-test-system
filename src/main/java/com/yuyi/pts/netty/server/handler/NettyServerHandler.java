package com.yuyi.pts.netty.server.handler;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    ServiceInterface serviceInterface = null;

    public NettyServerHandler(ServiceInterface serviceInterface){
        this.serviceInterface = serviceInterface;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        List<Param> outputList = serviceInterface.getOutput();
        ByteBuf buffer = ctx.alloc().buffer();
        // 车组号长度
        int trainGroupLength = 0;
        // 车组号长度
        int trainLength = 0;

        for (Param param : outputList) {
            int length = param.getLength();
            String value = param.getValue();
            FieldType type = param.getType();
            String field = param.getField();

            byte[] bytes = null;
            if (type == FieldType.Hex) {
                bytes = ByteUtils.hexString2Bytes(value);
            } else if (type == FieldType.Int) {
                bytes = ByteUtils.shortToByte2(Short.valueOf(value));
//                bytes = ByteUtils.storeInBytes(ByteUtils.intToBytesLow(Integer.parseInt(value)), 2);
            } else if (type == FieldType.String) {
                bytes = value.getBytes(StandardCharsets.UTF_8);
            } else if (type == FieldType.ASCII) {
                // TODO ASCII码存什么待定 根据车组号长度存入一个数组
                int i = Integer.parseInt(value);
                bytes = ByteUtils.asciiStrToBytes(value);
                if ("车组号".equals(field)) {
                    length = trainGroupLength;
                } else if ("车次号".equals(field)) {
                    length = trainLength;
                }
            }
            if ("车组号长度".equals(field)) {
                trainGroupLength = Integer.parseInt(param.getValue());
            } else if ("车次号长度".equals(field)) {
                trainLength = Integer.parseInt(param.getValue());
            }
            if (bytes != null) {
                byte[] outBytes = ByteUtils.storeInBytesLow(bytes, length);
                buffer.writeBytes(outBytes);
            }
        }
        ctx.channel().writeAndFlush(buffer);
    }
}
