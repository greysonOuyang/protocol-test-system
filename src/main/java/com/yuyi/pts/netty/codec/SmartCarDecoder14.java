package com.yuyi.pts.netty.codec;

import com.yuyi.pts.common.constant.Constant;
import com.yuyi.pts.common.util.DateUtils;
import com.yuyi.pts.model.server.SmartCarProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  解码  解决TCP粘包拆包问题
 * @author JoyWu
 */
@Slf4j
@Component
public class SmartCarDecoder14 extends ByteToMessageDecoder {

    /**
     * <pre>
     * data_head为帧头；两个字节，固定为0xEF,0xEF Frame_Head
     * Total:总帧数；1个字节    Frame_Count
     * Index:当前帧，1个字节；  Frame_Index
     * data_len 表示后接的应用数据（data部分）的长度；两个字节，低字节在前，高字节在后  Data_len
     * </pre>
     */
    private static final int HEAD_LENGTH = 6;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) throws Exception {
        log.info("进入了14号线解码");
        if (buffer.readableBytes() >= HEAD_LENGTH) {
            // 记录包头开始的index
            int beginReader;
            while (true) {
                // 获取包头开始的index
                beginReader = buffer.readerIndex();
                // 标记包头开始的index
                buffer.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                // 若读取到data_head为帧头两个字节，0xEF,0xEF，说明读取当前字节流开始位置了
                if (buffer.readShort() == Constant.HEED_DATA) {
                    break;
                }
                // 未读到包头，略过一个字节
                // 每次略过，一个字节，去读取，包头信息的开始标记
                buffer.resetReaderIndex();
                buffer.readByte();
                // 当略过，一个字节之后，
                // 数据包的长度，又变得不满足，此时，应该结束。等待后面的数据到达
                if (buffer.readableBytes() < HEAD_LENGTH) {
                    return;
                }
            }
            //总帧数
            byte frameCount=buffer.readByte();
            //当前帧
            int frameIndex=buffer.readByte();
            //data数据长度
            byte[] dataLen=new byte[2];
            buffer.readBytes(dataLen);
            // 获取data的字节流长度
            int dataLength=byte2ToShort(dataLen);
            // 判断请求数据包数据是否到齐
            if (buffer.readableBytes() < dataLength) {
                // 还原读指针
                buffer.readerIndex(beginReader);
                return;
            }
            //消息长度两个字节
            byte[] messageLen=new byte[2];
            buffer.readBytes(messageLen);
            int messageLength=byte2ToShort(messageLen);

            //消息发送时间 7个字节
            byte[] messageTime=new byte[7];
            buffer.readBytes(messageTime);
            String dateDime = DateUtils.getTime(messageTime);

            //线路号 2个字节
            byte[] line_id=new byte[2];
            buffer.readBytes(line_id);
            int lineId=byte2ToShort(line_id);

            //预留 18个字节
            byte[] spare=new byte[18];
            buffer.readBytes(spare);

            // 消息id
            byte messageId = buffer.readByte();

            // 版本号
            byte version=buffer.readByte();

            //data的content部分需要减去 消息长度两个字节 消息发送时间 线路号 预留 消息id版本号  data_tail为帧尾两个字节
            byte[] contentBytes =new byte[dataLength-31];
            buffer.readBytes(contentBytes);
            //data_tail为帧尾两个字节
            byte [] dataTail=new byte[2];
            buffer.readBytes(dataTail);
            SmartCarProtocol protocol = new SmartCarProtocol(frameCount,frameIndex,dataLength,
                    messageLength,dateDime,lineId,spare,messageId,contentBytes);
            out.add(protocol);
            // 回收已读字节
            buffer.discardReadBytes();
        }

    }
    /**
     *
     * <pre>
     * 长度为2的8位byte数组转换为一个16位short数字(低位在前，高位在后).
     * </pre>
     * @param arr
     * @return
     */
    public static short byte2ToShort(byte[] arr) {
        if (arr != null && arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return (short) (((short) arr[1] << 8) | ((short) arr[0] & 0xff));
    }

}