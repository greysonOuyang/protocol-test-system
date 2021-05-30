package com.yuyi.pts.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JoyWu
 */
@Slf4j
public class BinaryWebSocketHandler extends ModbusRequestHandler{

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws
    Exception {
		log.error("handler出现错误：{}", cause.getMessage());
	}
}