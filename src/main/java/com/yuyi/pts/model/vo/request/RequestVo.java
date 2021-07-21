package com.yuyi.pts.model.vo.request;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.model.vo.InterfaceVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestVo {
    /**
     * 端口
     */
    private int port;

    /**
     * ip地址
     */
    private String host;

    private String mode;

    /**
     * 接口Id
     */
    private String interfaceId;

    /**
     * 列车趟数 计划信息
     */
    private String trainCount;

    /**
     * 站台趟数 计划信息
     */
    private String stationCount;

    private String interfaceName;

    private String messageTypeId;

}
