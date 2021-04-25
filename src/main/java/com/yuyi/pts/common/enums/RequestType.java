package com.yuyi.pts.common.enums;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/11
 */
public enum RequestType {
    /** HTTP类型 */
    HTTP,
    /** WebSocket类型 */
    WebSocket,
    /** TCP类型 */
    TCP,
    /** UDP */
    UDP,
    /** 自定义 */
    Custom;

    /**
     * 应用层协议类型
     */
    public enum ProtocolType {
        /**
         *
         */
        none,
        /**
         * modbus
         */
        modbus,
        /**
         * 广州综合监控
         */
        gzIscs,
        /**
         * 清远ATS
         */
        qyAts;

    }
}
