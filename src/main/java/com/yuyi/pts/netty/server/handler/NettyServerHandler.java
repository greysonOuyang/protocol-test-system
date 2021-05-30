package com.yuyi.pts.netty.server.handler;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.enums.InterfaceMessageType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.netty.ChannelSupervise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 服务器处理程序
 *
 * @author greyson
 * @since 2021/4/27
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static Map<String, Object> RETURN_MAP = new HashMap<String, Object>();

    ServiceInterface serviceInterface = null;

    public NettyServerHandler(ServiceInterface serviceInterface){
        this.serviceInterface = serviceInterface;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.debug("客户端加入连接："+ctx.channel());
        ChannelSupervise.addChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
         Map<String, Object> messageMap = new HashMap<String, Object>();

        super.channelRead(ctx, msg);
        messageMap.put("input", msg);
        List<Param> outputList = serviceInterface.getOutput();
        String interfaceType = serviceInterface.getInterfaceType();
        byte[] sourceByteArr = null;
        // 写入消息类型 供编码使用 一个自己
        String messageType = InterfaceMessageType.stream()
                .filter(d -> d.getDescription().equals(interfaceType))
                .findFirst()
                .get().getType();
        sourceByteArr = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(messageType), 2);
        // 写入数据，组织成对方想要的数据
        for (Param param : outputList) {
            // 当前字段长度
            int currentFieldLength = param.getLength();
            String value = param.getValue().trim();
            FieldType type = param.getType();
            // 发给对方时的字节数, 默认是当前参数自带的length字段
            int storeLength = currentFieldLength;
            byte[] tempBytes = null;
            // 解析数据转成字节数组bytes
            if (type == FieldType.Hex) {
                tempBytes = ByteUtils.hexString2Bytes(value);
            } else if (type == FieldType.Int) {
                tempBytes = ByteUtils.decimalStrToHex(value);
            } else if (type == FieldType.String) {
                tempBytes = value.getBytes(StandardCharsets.UTF_8);
            } else if (type == FieldType.ASCII) {
                tempBytes = ByteUtils.asciiToHex(value);
                int length = tempBytes.length;
                // 如果是ascii码形式，计算当前数据长度并赋值给上一个参数的长度字段
                sourceByteArr[sourceByteArr.length - 1] = ByteUtils.intToByte(length);
                storeLength = length;
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
                tempBytes = ByteUtils.storeInBytes(tempBytes, storeLength);
                if (sourceByteArr == null) {
                    sourceByteArr = tempBytes;
                } else {
                    sourceByteArr = ByteUtils.addByteArrays(sourceByteArr, tempBytes);
                }
            }
        }
        messageMap.put("output", sourceByteArr);
        RETURN_MAP.put(UUID.randomUUID().toString(), messageMap);
        ctx.channel().writeAndFlush(sourceByteArr);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.debug("客户端断开连接："+ctx.channel());
        ChannelSupervise.removeChannel(ctx.channel());
    }
}
