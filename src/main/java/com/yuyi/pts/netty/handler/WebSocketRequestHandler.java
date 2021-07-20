package com.yuyi.pts.netty.handler;

import com.yuyi.pts.common.enums.OperationCommand;
import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.vo.response.ResponseInfo;
import com.yuyi.pts.service.ResponseService;
import com.yuyi.pts.service.impl.ResponseServiceImpl;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义WebSocket协议处理器、需要将ChannelHandlerContext作为全局属性且是静态；
 * 其他协议的自定义Handler类同，辅助NettyClient在发送消息时获取到ctx
 *
 * @author greyson
 * @since 2021/4/9
 */
@Slf4j
@Component
public class WebSocketRequestHandler extends SimpleChannelInboundHandler<Object> {
    public static ChannelHandlerContext myCtx;
    WebSocketClientHandshaker handShaker;
    ChannelPromise handshakeFuture;

    public static ResponseService responseService;

    static {
        responseService = SpringUtils.getBean(ResponseServiceImpl.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myCtx = ctx;
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("等待握手完成：{} ", this.handShaker.isHandshakeComplete());
        Channel ch = ctx.channel();
        FullHttpResponse response;
        if (!this.handShaker.isHandshakeComplete()) {
            try {
                response = (FullHttpResponse)msg;
                //握手协议返回，设置结束握手
                this.handShaker.finishHandshake(ch, response);
                //设置成功
                this.handshakeFuture.setSuccess();
                log.info("WebSocket Client connected! response headers[sec-websocket-extensions]:{}", response.headers());
            } catch (WebSocketHandshakeException var7) {
                FullHttpResponse res = (FullHttpResponse)msg;
                String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status(), res.content().toString(CharsetUtil.UTF_8));
                this.handshakeFuture.setFailure(new Exception(errorMsg));
            }
        } else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        } else {
//            TODO 暂时只支持文本消息发送 其他消息待做
            WebSocketFrame frame = (WebSocketFrame) msg;
            String content = "";
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                content = textFrame.text();
            } else if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;
            } else if (frame instanceof PongWebSocketFrame) {

            } else if (frame instanceof CloseWebSocketFrame) {
                ch.close();
            }
            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setState(1);
            responseInfo.setBody(content);
            String result = ResultUtil.successWithData(OperationCommand.TEST_LOG_RESPONSE, responseInfo);
            responseService.sendTextMsg(ctx, result);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }
    public WebSocketClientHandshaker getHandShaker() {
        return handShaker;
    }

    public void setHandShaker(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }
}
