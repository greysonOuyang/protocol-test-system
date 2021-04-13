package com.yuyi.pts.common.util;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.enums.OperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * 统一处理响应结果
 *
 * @author greyson
 * @since 2021/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILED ="FAILED";

    /**
     * 操作指令
     */
    private OperationCommand operationCommand;

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
    private T data;

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData(OperationCommand operationCommand) {
        return new ResultEntity<Type>(operationCommand, SUCCESS,null,  null);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     * @param data
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(OperationCommand operationCommand, Type data) {
        return new ResultEntity<Type>(operationCommand, SUCCESS, null,  data);
    }
    /**
     * 请求处理失败后使用的工具方法，返回指令类型，失败消息
     * @param message
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> failedWithMsg(OperationCommand operationCommand, String message) {
        return new ResultEntity<>(operationCommand, FAILED, message, null);
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
    public static <Type> ResultEntity<Type> failedWithMsgAndData(OperationCommand operationCommand, String message, Type data) {
        return new ResultEntity<>(operationCommand, FAILED, message, data);
    }

    /**
     * 请求失败，只返回指令类型
     *
     * @param operationCommand
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> failedWithoutNothing(OperationCommand operationCommand) {
        return new ResultEntity<>(operationCommand, FAILED, null, null);
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
