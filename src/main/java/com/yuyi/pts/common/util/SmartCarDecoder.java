package com.yuyi.pts.common.util;

import com.yuyi.pts.common.constant.ConstanValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码 -------公司系统copy而来
 *
 * @author wzl
 * @since 2021/4/16
 */
public class SmartCarDecoder extends ByteToMessageDecoder {

    /**
     * <pre>
     * data_head为帧头；两个字节，固定为0xEF,0xEF
     * Total:总帧数；1个字节
     * Index:当前帧，1个字节；
     * data_len 表示后接的应用数据（data部分）的长度；两个字节，低字节在前，高字节在后
     * Dev_Status表示设备当前主/备机状态，0x01:当前为主机，0x02:当前为备机1个字节；
     * </pre>
     */
    private static final int HEAD_LENGTH = 7;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) throws Exception {
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
                if (buffer.readShort() == ConstanValue.HEED_DATA) {
                    break;
                }
                // 未读到包头，略过一个字节
                // 每次略过，一个字节，去读取，包头信息的开始标记
                buffer.resetReaderIndex();
                buffer.readByte();
                // 当略过，一个字节之后，
                // 数据包的长度，又变得不满足
                // 此时，应该结束。等待后面的数据到达
                if (buffer.readableBytes() < HEAD_LENGTH) {
                    return;
                }
            }
            //总帧数
            byte total = buffer.readByte();
            //当前帧
            int index = buffer.readByte();
            //data数据长度
            byte[] data_len = new byte[2];
            buffer.readBytes(data_len);
            //备机状态
            int status = buffer.readByte();
            // 获取data的字节流长度
            short dataLength = byte2ToShort(data_len);
            // 判断请求数据包数据是否到齐
            if (buffer.readableBytes() < dataLength) {
                // 还原读指针
                buffer.readerIndex(beginReader);
                return;
            }
            //消息类型两个字节
            //消息类型低字节
            int typeLow = buffer.readByte();
            //消息类型高字节
            int typeHigh = buffer.readByte();
            //data的conten部分需要减去消息类型两个字节和data_tail为帧尾两个字节
            byte[] contentBytes = new byte[dataLength - 2];
            buffer.readBytes(contentBytes);
            //data_tail为帧尾两个字节
/*            byte [] data_tail=new byte[2];
            buffer.readBytes(data_tail);*/
            SmartCarProtocol protocol = new SmartCarProtocol(total, index, dataLength, typeLow, contentBytes);
            out.add(protocol);
            // 回收已读字节
            buffer.discardReadBytes();
        }


    }

    /**
     * <pre>
     * 长度为2的8位byte数组转换为一个16位short数字(低位在前，高位在后).
     * </pre>
     *
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