package com.yuyi.pts.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessageBody {
	private int code;
	private String body;

}
