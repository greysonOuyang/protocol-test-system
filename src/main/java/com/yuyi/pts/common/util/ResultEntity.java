package com.yuyi.pts.common.util;

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
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILD="FAILD";

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
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<Type>(SUCCESS, null, null);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     * @param data
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<Type>(SUCCESS, null, data);
    }
    /**
     * 请求处理失败后使用的工具方法
     * @param message
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> faild(String message) {
        return new ResultEntity<>(FAILD, message, null);
    }
}
