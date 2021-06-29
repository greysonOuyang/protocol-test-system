package com.yuyi.pts.common.constant;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/11
 */
public class ConstantValue {
    /** 常量code */
    public final static String CODE = "code";
    /** 常量msg */
    public final static String MSG = "msg";
    /** 常量data */
    public final static String DATA = "data";
    /** 常量result */
    public final static String RESULT = "result";

    public static final String CLIENT = "client";

    public static final String SERVER = "server";

    /**
     * 自定义协议头
     */
    public static final int HEED_DATA = -4113;
    /**
     * 自定义协议头
     */
    public static final int TAIL_DATA = -515;

    /** 常量code */
    public final static String PLAN_INFO = "planInfo";

    public static final int SERVER_READ_IDLE_TIME_OUT = 3;
    public static final int SERVER_WRITE_IDLE_TIME_OUT = 0;

    public static final int SERVER_ALL_IDLE_TIME_OUT = 0;

    /**
     * modbus功能码 读数据 十六进制
     */
    public static final String MODBUS_READ = "04";

    /**
     * modbus功能码 写数据 十六进制
     */
    public static final String MODBUS_WRITE = "10";


}
