package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.protocol.SmartCarProtocol;
import com.yuyi.pts.netty.client.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wzl
 * @date : 2021/4/16/14:21
 * @description: Modbus协议处理器
 */
@Component
@Slf4j
public class ModbusRequestHandler extends ChannelInboundHandlerAdapter {

    public static ChannelHandlerContext myCtx;
    Channel channel = null;
    ChannelHandlerContext ctx = null;


    private NettyClient client;
    private ByteBuf buf = Unpooled.buffer();

    private Map<String, List<String>> atsinfo = new HashMap<String, List<String>>();

    public void send(ByteBuf buf) {
        if (buf != null && ctx != null) {
            ctx.writeAndFlush(buf);
            log.info("modbus服务端发送命令成功");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channel = null;
        log.info("channel连接断开==");
        /**
         * 错误日志
         * LOG_ERROR:错误，不是非常紧急，在一定时间内修复即可。
         * @param tag      产生信息的程序名称
         * @param ip       该日志产生的IP
         * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
         * @param message  操作描述
         * @param operator 若没有具体用户，就是进程自身
         * @param date     该日志记录时间
         * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
         * @param optObj   操作对象(可能没有）
         * @param param    其他参数
         */
        String tag = "综合监控ATS信号源";
        String IP = null;
        String message = "ats信号源丢失";
        String operator = tag;
        Date date = new Date();
       // SyslogAPI.error(tag, IP, MsgTypeEnum.RUN_LOG, message, operator, date, ActionEnum.OTHER, null);
  //      client.doConnect();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
            log.debug("已经 3s 没有读到数据了");
            WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
            session.close();
            ctx.channel().close();
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 用于获取客户端发来的数据信息
        SmartCarProtocol body = (SmartCarProtocol) msg;
        if (body.getType() == 3) {
            buf.writeBytes(body.getContent());
            if (body.getIndex() == body.getTotal()) {
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
             //   atsService.HandleATSByteData(bytes);
            }
        }
        ByteBuf byteBuf = Unpooled.buffer();
    //    byte[] head_data = ByteUtils.shortToByte2(body.getHead_data());
        byte total = (byte) body.getTotal();
        byte index = (byte) body.getIndex();
        byte[] data_len = shortToByte2(body.getData_len());
        byte status = (byte) body.getStatus();
        byte type = (byte) body.getType();
        byte[] content = body.getContent();
     //   byte[] data_tail = ByteUtils.shortToByte2(body.getData_tail());
    //    byteBuf.writeBytes(head_data);
        byteBuf.writeByte(total);
        byteBuf.writeByte(index);
        byteBuf.writeBytes(data_len);
        byteBuf.writeByte(status);
        byteBuf.writeByte(type);
        byteBuf.writeBytes(content);
   //     byteBuf.writeBytes(data_tail);
       //当客户端与服务端的通道是连接上时才透传数据
//        if (ServerChannelInHandler.channel != null && ServerChannelInHandler.channel.isActive()) {
//            ServerChannelInHandler.channel.writeAndFlush(byteBuf);
//        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 心跳信息
     *
     * @return
     */
    public ByteBuf heart() {
        ByteBuf response = Unpooled.buffer();
        byte[] head = new byte[2];
        head[0] = (byte) 0xEF;
        head[1] = (byte) 0xEF;
        response.writeBytes(head);
        byte[] total = new byte[1];
        total[0] = 0x01;
        response.writeBytes(total);
        byte[] index = new byte[1];
        index[0] = 0x01;
        response.writeBytes(index);
        byte[] data_len = new byte[2];
        data_len[0] = 0x06;
        data_len[0] = 0x00;
        response.writeBytes(data_len);
        byte[] dev_Status = new byte[1];
        dev_Status[0] = 0x01;
        response.writeBytes(dev_Status);
        byte[] type = new byte[2];
        type[0] = 0x01;
        type[1] = 0x00;
        response.writeBytes(type);
        byte[] heart_flag = new byte[4];
        heart_flag[0] = (byte) 0xFF;
        heart_flag[1] = (byte) 0xFF;
        heart_flag[2] = (byte) 0xFF;
        heart_flag[3] = (byte) 0xFF;
        response.writeBytes(heart_flag);
        byte[] data_tail = new byte[2];
        data_tail[0] = (byte) 0xFD;
        data_tail[1] = (byte) 0xFD;
        response.writeBytes(data_tail);
        return response;
    }

    /**
     * 请求命令
     *
     * @return
     */
    public ByteBuf request(byte[] type, byte[] flag) {
        ByteBuf response = Unpooled.buffer();
        byte[] head = new byte[2];
        head[0] = (byte) 0xEF;
        head[1] = (byte) 0xEF;
        response.writeBytes(head);
        byte[] total = new byte[1];
        total[0] = 0x01;
        response.writeBytes(total);
        byte[] index = new byte[1];
        index[0] = 0x01;
        response.writeBytes(index);
        byte[] data_len = new byte[2];
        data_len[0] = 0x06;
        data_len[0] = 0x00;
        response.writeBytes(data_len);
        byte[] dev_Status = new byte[1];
        dev_Status[0] = 0x01;
        response.writeBytes(dev_Status);
        response.writeBytes(type);
        response.writeBytes(flag);
        byte[] data_tail = new byte[2];
        data_tail[0] = (byte) 0xFD;
        data_tail[1] = (byte) 0xFD;
        response.writeBytes(data_tail);
        return response;
    }

    /**
     * <pre>
     * 将一个16位的short转换为长度为2的8位byte数组.
     * </pre>
     * 高位在前低位在后
     *
     * @param s
     * @return
     */
    public static byte[] shortToByte2(Short s) {
        byte[] arr = new byte[2];
        arr[1] = (byte) (s >> 8);
        arr[0] = (byte) (s & 0xff);
        return arr;
    }
}
