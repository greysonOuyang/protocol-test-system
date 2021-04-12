package com.yuyi.pts.common.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端组织的请求数据
 *
 * @author greyson
 * @since 2021/4/11
 */
@Data
public class RequestDTO {
    /** 请求指令码 */
    public int code;

    /** 请求消息 */
    public  String msg;

    /** 请求数据 */
    public DataContentRequest data;
}
