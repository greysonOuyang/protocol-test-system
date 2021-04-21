package com.yuyi.pts.protocol.modbus.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 配合ModbusCodec使用 解决拆包粘包问题，先不使用，不确定数据长度
 *
 * @author greyson
 * @since 2021/4/19
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder() {
        this(102400, 4, 2, 1, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
