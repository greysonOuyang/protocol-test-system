package com.yuyi.pts.protocol.modbus.model;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author greyson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessageHeader {

	/**
	 * 业务标识符
	 */
	private ByteBuf affairIdentification;
	/**
	 * 协议标识符
	 */
	private ByteBuf protocolIdentification;
	/**
	 * 长度标识符
	 */
	private ByteBuf length;
	/**
	 * 单元标识符
	 */
	private ByteBuf unitIdentification;


}
