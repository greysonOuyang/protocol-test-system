package com.yuyi.pts.model.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerRequestDto {
    /**
     * 端口
     */
    private int port;

    /**
     * 接口Id
     */
    private String interfaceId;
}
