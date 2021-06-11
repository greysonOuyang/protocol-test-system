package com.yuyi.pts.model.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerRequestDto implements Serializable {
    /**
     * 端口
     */
    private int port;

    private String host;

    /**
     * 接口Id
     */
    private String interfaceId;
}
