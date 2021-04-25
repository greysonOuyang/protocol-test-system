package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.vo.response.ResponseInfo;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author : wzl
 * @date : 2021/4/20/9:22
 * @description:
 */
@Slf4j
public class UdpRequestHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    public static ChannelHandlerContext myCtx;

    public static ResponseService responseService;

    static {
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("Tcp处理器已经被添加");
        myCtx = ctx;
        super.handlerAdded(ctx);
    }
    //接受服务端发送的内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        log.info("进入了udp请求处理器");
        ResponseInfo responseInfo = new ResponseInfo();
        String  body =  msg.content().toString(CharsetUtil.UTF_8);
        responseInfo.setState(1);
        responseInfo.setBody(body);
        String result = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
        responseService.sendTextMsg(ctx, result);
        System.out.println(body+"这是服务端发送的内容");
        //这里接收到服务端发送的1内容
    }

    //捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }    @Override
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
}

//public class UdpRequestHandler extends ChannelInboundHandlerAdapter {
//    public static ChannelHandlerContext myCtx;
//
//    public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
//
//        //接受服务端发送的内容
//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
//            String  body =  msg.content().toString(CharsetUtil.UTF_8);
//            System.out.println(body+"这是服务端发送的内容");
//            //这里接收到服务端发送的1内容
//        }
//
//        //捕获异常
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
//                throws Exception {
//            cause.printStackTrace();
//            ctx.close();
//        }
//    }


//    public static ResponseService responseService;
//
//    static {
//        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
//    }
//
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        log.info("UDP处理器已经被添加");
//        myCtx = ctx;
//        super.handlerAdded(ctx);
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
//        ctx.flush();
//
//    }
//
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("服务端发回的数据：{}", msg);
//        ResponseInfo responseInfo = new ResponseInfo();
//        responseInfo.setState(1);
//        responseInfo.setBody((String) msg);
//        String result = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
//        UDPUtil.multicastSend(result.getBytes());
//        log.info("----组播下发完成----");
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("UDPhandler出现错误：{}", cause.getMessage());
//    }
//}
