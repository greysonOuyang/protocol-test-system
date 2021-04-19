package com.yuyi.pts.protocol.modbus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author greyson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessageBody {
	/**
	 * 功能码字段
	 */
	private int code;

	/**
	 * 数据字段
	 */
	private String body;

}
