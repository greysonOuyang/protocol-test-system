package com.yuyi.pts.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作辅助类
 * @author zhangxs
 */
public class DateUtils {

	/**
	 * 日期转化
	 * @param date  日期字符串
	 * @param format 格式化
	 * @return
	 */
	public static Date parseDate(String date,String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("无法解析日期："+date+",格式："+format);
		}
	}
	/**
	 * 解析时间 类型
	 */
	public static String getTime(byte[] bytes) {
		String time = "";
		//年
		byte[] bytes1 = new byte[2];
		bytes1[0] = bytes[0];
		bytes1[1] = bytes[1];
		String year = String.valueOf(ByteUtils.byte2ToShort(bytes1));
		//月
		byte[] bytes2 = new byte[1];
		bytes2[0] = bytes[2];
		String mothon = String.valueOf(ByteUtils.byteToShort(bytes2));
		//日
		byte[] bytes3 = new byte[1];
		bytes3[0] = bytes[3];
		String data = String.valueOf(ByteUtils.byteToShort(bytes3));
		//时
		byte[] bytes4 = new byte[1];
		bytes4[0] = bytes[4];
		String hour = String.valueOf(ByteUtils.byteToShort(bytes4));
		//分
		byte[] bytes5 = new byte[1];
		bytes5[0] = bytes[5];
		String minute = String.valueOf(ByteUtils.byteToShort(bytes5));
		//分
		byte[] bytes6 = new byte[1];
		bytes6[0] = bytes[6];
		String second = String.valueOf(ByteUtils.byteToShort(bytes6));
		time=year+"-"+mothon+"-"+data+" "+hour+":"+minute+":"+second;
		return time;
	}


}
