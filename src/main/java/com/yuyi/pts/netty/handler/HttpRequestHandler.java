package com.yuyi.pts.netty.handler;


import com.yuyi.pts.common.cache.CtxWithWebSocketSessionCache;
import com.yuyi.pts.common.cache.ObjCache;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.common.vo.response.ResponseInfo;
import com.yuyi.pts.service.ProcessResponseService;
import com.yuyi.pts.service.impl.ProcessResponseServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
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
    private static ProcessResponseService processResponseService;

    static {
        processResponseService = SpringUtils.getBean(ProcessResponseServiceImpl.class);
    }
    public static ChannelHandlerContext myCtx;
    public static ChannelFuture future;
    private ChannelPromise promise;
    private RequestDataDto response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myCtx = ctx;
        FullHttpRequest request = null;
        Object url = ObjCache.get("uri");
        // 获取到发送请求方式
        HttpMethod method = HttpMethod.valueOf(ObjCache.get("method").toString());
        request= new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, url.toString());
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        ctx.writeAndFlush(request);
        super.channelActive(ctx);
        log.info("客户端已经被激活:" + ctx.channel().remoteAddress().toString());
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
                String responseData = ResultEntity.successWithData(response.status().code(), responseInfo);
                WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
                session.sendMessage(new TextMessage(responseData));
            }else {
                String responseData = ResultEntity.failedWithMsg(response.status().code(), "请求失败，请稍后重试");
                WebSocketSession session = CtxWithWebSocketSessionCache.get(ctx);
                session.sendMessage(new TextMessage(responseData));
            }
            System.out.println("response -> "+result);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
