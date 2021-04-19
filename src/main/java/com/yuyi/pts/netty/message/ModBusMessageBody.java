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
public class ModBusMessageBody {
	private ByteBuf code;
	private ByteBuf body;

	public ByteBuf getBuf() {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeBytes(ByteBufUtils.getByteBuf(code));
		buffer.writeBytes(ByteBufUtils.getByteBuf(body));
		return buffer;
	}
}
