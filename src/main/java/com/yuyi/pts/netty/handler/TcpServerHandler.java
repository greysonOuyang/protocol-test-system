package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.cache.ByteDataCache;
import com.yuyi.pts.common.protocol.ModBus2Message;
import com.yuyi.pts.common.protocol.ModBusBody;
import com.yuyi.pts.common.protocol.ModBusHeader;
import com.yuyi.pts.common.util.ByteBufUtils;
import com.yuyi.pts.common.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.BitSet;

/**
 * NettyServer的关于TCP的处理器
 *
 * @author greyson
 * @since 2021/6/25
 */
@Slf4j
public class TcpServerHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 初始化点表大小 以及赋值
        BitSet bitSet = new BitSet(1904);
        for (int i = 0; i <=1904; i++) {
            if (i%2 == 0) {
                bitSet.set(i, true);
            }
        }
        byte[] byteData = bitSet.toByteArray();
        ByteDataCache.updateRegister(119, 1, byteData);
        log.info("初始化闸机状态点表");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ModBus2Message modBusMessage = (ModBus2Message) msg;
        byte[] bodyData = modBusMessage.getBodyData();
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bodyData);

        //功能码
        byte[] functionCode = ByteBufUtils.parseByteCount(buffer,1);
        String functionCodeStr = ByteUtils.byteArrayToHexStr(functionCode);

        // 起始地址
        byte[] startAddressByte = ByteBufUtils.parseByteCount(buffer, 2);
        int startAddress = ByteUtils.byte2ToShort(startAddressByte);
        // 寄存器个数
        byte[] registerCounterByte = ByteBufUtils.parseByteCount(buffer, 2);
        int registerCounters = ByteUtils.byte2ToShort(registerCounterByte);

        ModBus2Message response = new ModBus2Message();
        ModBusHeader header = modBusMessage.getHeader();
        ModBusBody body = new ModBusBody();
        body.setFunctionCode(functionCodeStr);
        body.setIsResponse(true);
        if ("04".equals(functionCodeStr)) {
            byte[] cacheByte = ByteDataCache.getRegister(registerCounters, startAddress);
            body.setByteCount(cacheByte.length);
            body.setData(cacheByte);
        }
        // 响应数据
        response.setHeader(header);
        response.setBody(body);
        ctx.writeAndFlush(response);
    }
}
