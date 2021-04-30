package com.yuyi.pts.netty.server.handler;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    ServiceInterface serviceInterface = null;

    public NettyServerHandler(ServiceInterface serviceInterface){
        this.serviceInterface = serviceInterface;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ByteBuf out = ctx.alloc().buffer();
        List<Param> outputList = serviceInterface.getOutput();
        for (Param param : outputList) {
            int length = param.getLength();
            String value = param.getValue();
            FieldType type = param.getType();
            byte[] bytes = null;
            if (type == FieldType.Hex) {
                bytes = ByteUtils.hexString2Bytes(value);
            } else if (type == FieldType.String) {
                bytes = value.getBytes(StandardCharsets.UTF_8);
            }else if (type == FieldType.ASCII) {
                bytes = value.getBytes(StandardCharsets.UTF_8);
            }
            byte[] outBytes = ByteUtils.storeInBytes(bytes, length);

            out.writeBytes(outBytes);
        }
        ctx.channel().writeAndFlush(out);

    }
}
