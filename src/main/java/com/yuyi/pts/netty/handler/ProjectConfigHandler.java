package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.server.SmartCarProtocol;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 根据配置生成信息的处理器
 *
 * @author greyson
 * @since 2021/4/27
 */
@Slf4j
public class ProjectConfigHandler extends ChannelInboundHandlerAdapter {

    static SmartCarProtocol protocol;
    public static ResponseService responseService;


    static {
        protocol = SpringUtils.getBean(SmartCarProtocol.class);
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);

    }


    TInterfaceConfig serviceInterface = null;

    /**
     * 模式 client或者 Server
     */
    String mode;

    public ProjectConfigHandler(TInterfaceConfig serviceInterface, String mode) {
        this.serviceInterface = serviceInterface;
        this.mode = mode;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.debug("客户端加入连接：" + ctx.channel());
        if (Constant.CLIENT.equals(mode)) {
            byte[] sourceByteArr = buildMsg();
            ScheduledThreadPoolUtil.scheduleAtFixedRateByTime(()-> {
                ctx.channel().writeAndFlush(sourceByteArr);
            },0, 1, 10, TimeUnit.SECONDS);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("input", msg);
        super.channelRead(ctx, msg);
        byte[] sourceByteArr = buildMsg();
        messageMap.put("output", sourceByteArr);
        responseService.broadcast("/topic/response", messageMap);
        ctx.channel().writeAndFlush(sourceByteArr);
    }

    /**
     * 根据配置生成要发送的消息
     *
     * @return 字节数据
     */
    private byte[] buildMsg() {
        List<Param> outputList = serviceInterface.getOutput();
        byte[] sourceByteArr = buildMessageType();

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

            } else if (type == FieldType.Time) {
                ByteBuf byteBuf = Unpooled.buffer();
                byte[] timeByteArr = new byte[7];
                String[] timeStr = value.split("-");
                for (String str : timeStr) {
                    byte[] bytes = ByteUtils.shortToByte2SmallEnd(ByteUtils.intToShort(Integer.parseInt(str)));
                    if (bytes[1] == 0) {
                        bytes = ByteUtils.storeInBytes(bytes, 1);
                    }
                    byteBuf.writeBytes(bytes);
                }
                byteBuf.readBytes(timeByteArr);
                tempBytes = timeByteArr;
            }
            // 计算长度
            if (currentFieldLength == -1) {
                int length = tempBytes.length;
                // 如果是ascii码形式，计算当前数据长度并赋值给上一个参数的Value
                sourceByteArr[sourceByteArr.length - 1] = ByteUtils.intToByte(length);
                storeLength = length;
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
        return sourceByteArr;
    }

    /**
     * 12-18号线的接口需要传输MessageType
     */
    private byte[] buildMessageType() {
        byte[] sourceByteArr = null;
        if (serviceInterface.getRequestType() != null) {
            String interfaceType = serviceInterface.getRequestType();
              if(protocol.getLineId()==14){
                  sourceByteArr = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(interfaceType), 1);
              }else {
                  sourceByteArr = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(interfaceType), 2);
              }
        }
        return sourceByteArr;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.debug("客户端断开连接：" + ctx.channel());
    }
}
