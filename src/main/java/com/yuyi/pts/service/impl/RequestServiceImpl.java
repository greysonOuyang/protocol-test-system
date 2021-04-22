package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.service.RequestService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/15
 */
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());


    private FullHttpRequest request = null;
    @Override
    public void sendBinMessage(RequestType type,ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        // 发送时间间隔
        long interval = dataContent.getInterval();
        int value = new Long(interval).intValue();
        
        // tcp请求发数据给第三方
        if(type == RequestType.TCP){
            // 发送一次
//            Object body = dataContent.getBody();
//            byte[] bytes = SerializeUtil.serialize(body);
//            log.info("客户端往服务端发送的数据：" + body);
//            ByteBuf buffer = currentCtx.alloc().buffer();
//            buffer.writeBytes(bytes);
//            currentCtx.writeAndFlush(bytes);
            int count = dataContent.getCount();
            ScheduledThreadPoolUtil.scheduleDelayByNumber(() -> {
                Object body = dataContent.getBody();
                String result = (String) body;
                byte[] bytes = SerializeUtil.serialize(body);
                log.info("客户端往服务端发送的数据：" + result);
                currentCtx.writeAndFlush(result);
            }, 0, value, count, TimeUnit.MILLISECONDS);
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
            for (int i = 0; i < dataContent.getAverage(); i++) {
                // TODO 这里行多次发送消息给服务端
                currentCtx.writeAndFlush(request);
            }
        }else if(type == RequestType.ModBus){
            // TODO 这里行多次发送消息给服务端
            for (int i = 0; i < dataContent.getAverage(); i++) {
                currentCtx.writeAndFlush(dataContent);
            }
        }
    }
}
