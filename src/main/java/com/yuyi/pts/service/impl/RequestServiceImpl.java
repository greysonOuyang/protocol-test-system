package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import com.yuyi.pts.service.RequestService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 发送数据到服务端（测试接口系统）
 *
 * @author greyson
 * @since 2021/4/15
 */
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {


    @Override
    public void sendTextMessage(RequestType type, ChannelHandlerContext currentCtx, RequestDataDto dataContent) {
        // 发送时间间隔
        long interval = dataContent.getInterval();
        int value = new Long(interval).intValue();
        // 每个连接发送几次请求
        int count = dataContent.getAverage();
        String jsonString = JSONObject.toJSONString(dataContent.getBody());
        log.debug("客户端往服务端发送的数据：{}", jsonString);
        // 批量定时发送数据
        Object finalToBeSendContent = getToBeSendContent(type, dataContent);
//        TODO 在执行完所有的发送后对session以及服务进行关闭
        ScheduledThreadPoolUtil.scheduleDelayByNumber(() -> {
            currentCtx.writeAndFlush(finalToBeSendContent);
        }, 0, value, count, TimeUnit.MILLISECONDS);

    }

    /**
     * 构造发送出去的请求
     *
     * @param type 请求类型
     * @param dataContent 初始数据
     * @return toBeSendContent 待发送的数据
     */
    private Object getToBeSendContent(RequestType type, RequestDataDto dataContent) {
        Object toBeSendContent = null;
        if (type == RequestType.HTTP){
            // http给第三方发数据
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
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, urlResult, buf);
            request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            toBeSendContent = request;
        }  else if (type == RequestType.TCP || type == RequestType.UDP) {
            toBeSendContent = dataContent;
        } else if (type == RequestType.WebSocket) {
// TODO           待支持更多类型的消息 ping pong bin
            TextWebSocketFrame text = new TextWebSocketFrame((String) dataContent.getBody());
            toBeSendContent = text;
        }
        return toBeSendContent;
    }
}
