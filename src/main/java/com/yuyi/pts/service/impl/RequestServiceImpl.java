package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.common.util.SerializeUtil;
import com.yuyi.pts.common.vo.request.RequestDataDto;
import com.yuyi.pts.common.vo.request.SmartCarProtocol;
import com.yuyi.pts.service.RequestService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        }else if(type == RequestType.ModBus){
            ByteBuf buffer = currentCtx.alloc().buffer();
            getRequestProtocolDTO(currentCtx,buffer,dataContent);
        }
    }

    /**
     * 处理数据，封装消息
     * @param currentCtx buffer  dataContent
     * @return void
     */
    private void getRequestProtocolDTO(ChannelHandlerContext currentCtx,ByteBuf buffer,RequestDataDto dataContent) {
        SmartCarProtocol request = dataContent.getSmartCarProtocol();
        byte[] total = intToByteArray(request.getTotal());
        byte[] index = intToByteArray(request.getIndex());
        byte[] dataLength = intToByteArray(request.getData_len());
        byte[] status = intToByteArray(request.getStatus());
        byte[] dataType = intToByteArray(request.getType());
        byte[] content = request.getContent();
        buffer.writeBytes(total);
        buffer.writeBytes(index);
        buffer.writeBytes(dataLength);
        buffer.writeBytes(status);
        buffer.writeBytes(dataType);
        buffer.writeBytes(content);
        currentCtx.writeAndFlush(buffer);
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }
}
