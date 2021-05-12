package com.yuyi.pts.netty.server.handler;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
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
        // 粘包处理，组织成对方想要的数据
        byte[] sourceByteArr = null;

        for (Param param : outputList) {
            int length = param.getLength();
            String value = param.getValue().trim();
            FieldType type = param.getType();

            byte[] tempBytes = null;
            // 解析数据转成字节数组bytes
            if (type == FieldType.Hex) {
                tempBytes = ByteUtils.convertHEXString2ByteArray(value);
            } else if (type == FieldType.Int) {
                tempBytes = ByteUtils.decimalStrToHex(value);
            } else if (type == FieldType.String) {
                tempBytes = value.getBytes(StandardCharsets.UTF_8);
            } else if (type == FieldType.ASCII) {
                tempBytes = ByteUtils.asciiToHex(value);
                length = tempBytes.length;
            } else if (type == FieldType.Time) {
                // Todo 年份的解析存在问题，不确定要解析成何种数据，由于pis暂未使用这个字段，故不处理
                byte[] timeByteArr = new byte[7];
                String[] timeStr = value.split("-");
                for (String str : timeStr) {
                    String hex = Integer.toHexString(Integer.parseInt(str));
                    byte[] hexByte = ByteUtils.convertHEXString2ByteArray(hex);
                    timeByteArr = ByteUtils.addByteArrays(timeByteArr, hexByte);
                }

                tempBytes = timeByteArr;
            }

            if (tempBytes != null) {
                if (length != -1) {
                    tempBytes = ByteUtils.storeInBytesLow(tempBytes, length);
                }
                if (sourceByteArr == null) {
                    sourceByteArr = tempBytes;
                } else {
                    sourceByteArr = ByteUtils.addByteArrays(sourceByteArr, tempBytes);
                }
            }
        }
        log.info("sourceByteArr=====",sourceByteArr);
        ctx.channel().writeAndFlush(sourceByteArr);
    }
}
