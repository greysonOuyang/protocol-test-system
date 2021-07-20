package com.yuyi.pts.netty.handler;


import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.vo.response.ResponseInfo;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * HTTP(S)协议处理器、需要将ChannelHandlerContext作为全局属性且是静态；
 * 其他协议的自定义Handler类同，辅助NettyClient在发送消息时获取到ctx
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
@Slf4j
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    public static ChannelHandlerContext myCtx;

    public static com.yuyi.pts.service.ResponseService ResponseService;

    static {
        ResponseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myCtx = ctx;
        super.channelActive(ctx);
        log.info("HTTP客户端已经被激活:" + ctx.channel().remoteAddress().toString());
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("msg -> "+msg);
        if(msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse)msg;
            ByteBuf buf = response.content();
            String result = buf.toString(CharsetUtil.UTF_8);
            ResponseInfo responseInfo = new ResponseInfo();
            // 状态码
            responseInfo.setCode(response.status().code());
            // 响应体
            responseInfo.setBody(result);
            // 头信息
            responseInfo.setHeaders(response.headers());
            responseInfo.setState(response.status().code());
            // 成功执行 successWithData()
            if(200==response.status().code()){
                String responseData = ResultUtil.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
                WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
                session.sendMessage(new TextMessage(responseData));
            }else {
                String responseData = ResultUtil.failedWithMsg(OperationCommand.TEST_LOG_RESPONSE, "请求失败，请稍后重试");
                WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
                session.sendMessage(new TextMessage(responseData));
            }
            log.info("response -> {}", result);
        }
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
