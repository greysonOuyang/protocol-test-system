package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.model.protocol.SmartCarProtocol;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.model.vo.request.RequestVo;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.service.ResponseService;
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
import java.util.*;
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
public class ContextHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    ResponseService responseService;

    RequestVo requestVo;

    /**
     * 模式 client或者 Server
     */
    String mode;

    public ContextHandler(RequestVo requestVo, String mode) {
        this.requestVo = requestVo;
        this.mode = mode;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.channelFuture.addListener((ChannelFutureListener) future -> {
            //如果连接成功
            if (future.isSuccess()) {
                if (ConstantValue.CLIENT.equals(mode)) {
                    sendMsg(ctx);
                }
            }
        });
        //添加连接
        log.info("接口配置处理器已经激活：" + ctx.channel());

    }

    private void sendMsg(ChannelHandlerContext ctx) {
        String baseType = requestVo.getBaseType();
        String context = requestVo.getContext();
        byte[] bytes = null;
        if ("Hex".equals(baseType)) {
            String newContext = context.replaceAll(" ", "");
            bytes = ByteUtils.hexString2Bytes(newContext);
        } else if ("Dec".equals(baseType)) {
            String[] strArr = context.split(" ");
            bytes = new byte[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                bytes[i] = (byte) Integer.parseInt(strArr[i]);
            }
        }
        ctx.writeAndFlush(bytes);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        sendMsg(ctx);
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("input", msg);
        super.channelRead(ctx, msg);
        messageMap.put("output", "发送消息成功");
        responseService.broadcast("/topic/response", messageMap);
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
