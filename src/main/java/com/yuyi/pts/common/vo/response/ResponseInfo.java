package com.yuyi.pts.common.vo.response;

import com.alibaba.fastjson.JSONObject;

/**
 * 响应前端组织的信息
 * 
 * @author greyson
 * @since 2021/4/15
 */
public class ResponseInfo {
	/** 服务器响应的状态码 */
	private int code;
	/** 属于第几批请求 */
	private int count;
	/** 属于第几次请求 */
	private int index;
	/** 请求状态,0=失败,1=成功 */
	private int state;
	/** 响应的信息 */
	private String body;

	/**
	 * 将当前对象转换为JsonObject
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("code", getCode());
		result.put("count", getCount());
		result.put("index", getIndex());
		result.put("state", getState());
		if (getBody() != null) {
			result.put("body", getBody());
		}
		return result;
	}

	/**
	 * 获取服务器响应状态码
	 * 
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置服务器响应状态码
	 * 
	 * @param code
	 * @return
	 */
	public ResponseInfo setCode(int code) {
		this.code = code;
		return this;
	}

	/**
	 * 获取这是属于第几批请求
	 * 
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置这是属于第几批请求
	 * 
	 * @param count
	 * @return
	 */
	public ResponseInfo setCount(int count) {
		this.count = count;
		return this;
	}

	/**
	 * 获取请求状态
	 * 
	 * @return 0=失败,1=成功
	 */
	public int getState() {
		return state;
	}

	/**
	 * 获取属于第几次请求
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 设置属于第几次请求
	 * 
	 * @param index
	 * @return
	 */
	public ResponseInfo setIndex(int index) {
		this.index = index;
		return this;
	}

	/**
	 * 设置请求状态
	 * 
	 * @param state
	 *          0=失败,1=成功
	 * @return
	 */
	public ResponseInfo setState(int state) {
		this.state = state;
		return this;
	}

	/**
	 * 获取服务器响应信息
	 * 
	 * @return
	 */
	public String getBody() {
		return body;
	}

	/**
	 * 设置服务器响应信息
	 * 
	 * @param body
	 * @return
	 */
	public ResponseInfo setBody(String body) {
		this.body = body;
		return this;
	}

	@Override
	public String toString() {
		return "OstResponseInfo [code=" + code + ", count=" + count + ", index=" + index + ", state=" + state + ", body=" + body + "]";
	}

}
