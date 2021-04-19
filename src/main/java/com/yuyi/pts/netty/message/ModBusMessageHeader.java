package com.yuyi.pts.netty.message;

import com.yuyi.pts.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

	public ByteBuf getBuf() {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeBytes(ByteBufUtils.getByteBuf(affairIdentification));
		buffer.writeBytes(ByteBufUtils.getByteBuf(protocolIdentification));
		buffer.writeBytes(ByteBufUtils.getByteBuf(length));
		buffer.writeBytes(ByteBufUtils.getByteBuf(unitIdentification));
		return buffer;
	}
	
}
