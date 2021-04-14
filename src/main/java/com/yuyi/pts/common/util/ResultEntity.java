package com.yuyi.pts.common.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一处理响应结果
 *
 * @author greyson
 * @since 2021/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILED ="FAILED";

    /**
     * 操作指令
     */
    private int code;

    /**
     * 封装当前请求结果（失败、成功）
     */
    private String result;

    /**
     * 请求处理失败时返回的错误信息
     */
    private String message;

    /**
     * 要返回的数据
     */
    private Object data;

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     * @param <Type>
     * @return
     */
    public static String successWithoutData(int operationCommand) {
        return getJsonResult(new ResultEntity(operationCommand, SUCCESS,null,  null));
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     * @param data
     * @param <Type>
     * @return
     */
    public static String successWithData(int operationCommand, Object data) {
        return getJsonResult(new ResultEntity(operationCommand, SUCCESS, null, data));
    }
    /**
     * 请求处理失败后使用的工具方法，返回指令类型，失败消息
     * @param message
     * @param <Type>
     * @return
     */
    public static String failedWithMsg(int operationCommand, String message) {
        return getJsonResult(new ResultEntity(operationCommand, FAILED, message, null));
    }

    /**
     * 请求失败，返回指令类型、失败消息、失败后的数据
     *
     * @param operationCommand
     * @param message
     * @param data
     * @param <Type>
     * @return
     */
    public static String failedWithMsgAndData(int operationCommand, String message, Object data) {
        return getJsonResult(new ResultEntity(operationCommand, FAILED, message, data));
    }

    /**
     * 请求失败，只返回指令类型
     *
     * @param operationCommand
     * @return
     */
    public static String failedWithoutNothing(int operationCommand) {
        return getJsonResult(new ResultEntity(operationCommand, FAILED, null, null));
    }

    /**
     * 将响应结果转成json
     *
     * @param obj
     * @return
     */
    public static String getJsonResult(Object obj) {
        return JSON.toJSONString(obj);
    }

}
