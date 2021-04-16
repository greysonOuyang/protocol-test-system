package com.yuyi.pts.common.util;

import com.alibaba.fastjson.JSON;
import com.yuyi.pts.common.enums.OperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一处理响应结果 所有返回前端的信息拿此类进行封装 如果服务端有返回响应数据data，一定要拿ResponseInfo封装到body
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
     * 响应指令码
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
     * 要返回的数据 服务端返回的响应信息用ResponseInfo进行封装隔离
     */
    private Object data;

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     * @param <Type>
     * @return
     */
    public static String successWithoutData(OperationCommand operationCommand) {
        return getJsonResult(new ResultEntity(operationCommand.value(), SUCCESS,null,  null));
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     *
     * @param operationCommand
     * @param data
     * @return
     */
    public static String successWithData(OperationCommand operationCommand, Object data) {
        return getJsonResult(new ResultEntity(operationCommand.value(), SUCCESS, null, data));
    }
    /**
     * 请求处理失败后使用的工具方法，返回指令类型，失败消息
     * @param message
     * @param <Type>
     * @return
     */
    public static String failedWithMsg(OperationCommand operationCommand, String message) {
        return getJsonResult(new ResultEntity(operationCommand.value(), FAILED, message, null));
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
    public static String failedWithMsgAndData(OperationCommand operationCommand, String message, Object data) {
        return getJsonResult(new ResultEntity(operationCommand.value(), FAILED, message, data));
    }

    /**
     * 请求失败，只返回指令类型
     *
     * @param operationCommand
     * @return
     */
    public static String failedWithoutNothing(OperationCommand operationCommand) {
        return getJsonResult(new ResultEntity(operationCommand.value(), FAILED, null, null));
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
