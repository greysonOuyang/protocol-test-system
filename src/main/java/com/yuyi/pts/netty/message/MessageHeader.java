package com.yuyi.pts.netty.message;

import com.yuyi.pts.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {

	private ByteBuf affairInden;
	private ByteBuf protocalIdentif;
	private ByteBuf length;
	private ByteBuf unitident;

	public ByteBuf getBuf() {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeBytes(ByteBufUtils.getByteBuf(affairInden));
		buffer.writeBytes(ByteBufUtils.getByteBuf(protocalIdentif));
		buffer.writeBytes(ByteBufUtils.getByteBuf(length));
		buffer.writeBytes(ByteBufUtils.getByteBuf(unitident));
		return buffer;
	}
	
}
