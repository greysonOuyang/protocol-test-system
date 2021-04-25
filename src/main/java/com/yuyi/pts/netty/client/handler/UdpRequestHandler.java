package com.yuyi.pts.netty.client.handler;

import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.util.UDPUtil;
import com.yuyi.pts.model.vo.response.ResponseInfo;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzl
 * @date : 2021/4/20/9:22
 * @description:
 */
@Slf4j
public class UdpRequestHandler extends ChannelInboundHandlerAdapter {
    public static ChannelHandlerContext myCtx;
    public static ResponseService responseService;

    static {
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("UDP处理器已经被添加");
        myCtx = ctx;
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端发回的数据：{}", msg);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setState(1);
        responseInfo.setBody((String) msg);
        String result = ResultEntity.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
        UDPUtil.multicastSend(result.getBytes());
        log.info("----组播下发完成----");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("UDPhandler出现错误：{}", cause.getMessage());
    }
}
