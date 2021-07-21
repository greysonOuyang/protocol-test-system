package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.server.SmartCarProtocol;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
@Component
@Scope("prototype")
@NoArgsConstructor
public class ProjectConfigHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    SmartCarProtocol protocol;

    @Autowired
    ResponseService responseService;

    ProjectDto projectDto;

    /**
     * 模式 client或者 Server
     */
    String mode;

    public ProjectConfigHandler(ProjectDto projectDto, String mode) {
        this.projectDto = projectDto;
        this.mode = mode;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.channelFuture.addListener((ChannelFutureListener) future -> {
            //如果连接成功
            if (future.isSuccess()) {
                if (ConstantValue.CLIENT.equals(mode)) {
                    byte[] sourceByteArr = buildMsg();
                    ScheduledThreadPoolUtil.scheduleAtFixedRateByTime(()-> {
                        log.info("接口配置处理器发送消息");
                        ctx.channel().writeAndFlush(sourceByteArr);
                    },0, 10, 1, TimeUnit.MINUTES);
                }
            }
        });
        //添加连接
        log.info("接口配置处理器已经激活：" + ctx.channel());

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
        List<ParamEntity> outputList = projectDto.getOutput();
        byte[] sourceByteArr = buildMessageType();

        // 写入数据，组织成对方想要的数据
        for (ParamEntity param : outputList) {
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
        if (projectDto.getMessageTypeEntity() != null && projectDto.getMessageTypeEntity().getMessageType() != null) {
            String interfaceType = projectDto.getMessageTypeEntity().getMessageType();
            if (protocol.getLineId() == 14) {
                sourceByteArr = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(interfaceType), 1);
            } else {
                sourceByteArr = ByteUtils.storeInBytesLow(ByteUtils.hexString2Bytes(interfaceType), 2);
            }
        }
        return sourceByteArr;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.debug("与[{}]的连接断开", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("处理消息出现了异常：[{}]", cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
