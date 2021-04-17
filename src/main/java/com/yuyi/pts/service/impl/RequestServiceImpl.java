package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.cache.ObjCache;
import com.yuyi.pts.common.cache.xxObj;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import com.yuyi.pts.service.RequestService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/15
 */
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private FullHttpRequest request = null;
    @Override
    public void sendBinMessage(RequestType type,ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        // tcp请求发数据给第三方
        if(type == RequestType.TCP){
            Object body = dataContent.getBody();
            byte[] bytes = SerializeUtil.serialize(body);
            log.info("客户端往服务端发送的数据：" + body);
            ByteBuf buffer = currentCtx.alloc().buffer();
            buffer.writeBytes(bytes);
            currentCtx.writeAndFlush(bytes);
        }
        // http给第三方发数据
        else if (type == RequestType.HTTP){
            String url = dataContent.getUrl();
            String flag = url.substring(5,5);
            String urlResult = null;
            if("s".equals(flag)){
                urlResult = url.substring(22);
            }else {
                 urlResult = url.substring(21);
            }
            String msg = dataContent.getBody().toString();
            byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
            ByteBuf buf = Unpooled.wrappedBuffer(bytes);
            dataContent.getMethod();
            HttpMethod method = HttpMethod.valueOf(dataContent.getMethod().name());
            request= new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, urlResult, buf);
            request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
            currentCtx.writeAndFlush(request);
        }

    }
}
