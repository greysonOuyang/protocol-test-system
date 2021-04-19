package com.yuyi.pts.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 配合ModbusCodec使用
 *
 * @author greyson
 * @since 2021/4/19
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
