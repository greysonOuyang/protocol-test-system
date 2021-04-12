package com.yuyi.pts.netty.handler;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.vo.request.RequestDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 初始化处理器，对前端传过来的指令进行解析
 *
 * @author greyson
 * @since 2021/4/11
 */
@Slf4j
@Component
public class InitialHandler extends SimpleChannelInboundHandler<RequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestDTO requestDTO) throws Exception {
        Channel currentChannel = ctx.channel();
        //获取客户端传输过来的消息
        Object body = requestDTO.getData().getRequestData().getBody();
        String content = JSON.toJSONString(body);
        log.info("[websocket]主机--{}接收到的消息：{}", ctx.channel().remoteAddress(), content);
        Integer code = requestDTO.getCode();

        // TODO 根据消息类型选择不同处理
//        if (OptionCommand.CANCEL.equals(code)) {
//            // 取消命令的业务操作，下同
//        } else if (code.equals(OptionCommand.SUBMIT_TEST.value())) {
//
//        }
    }
}
