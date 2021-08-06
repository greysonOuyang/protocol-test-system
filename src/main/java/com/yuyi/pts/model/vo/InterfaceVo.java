package com.yuyi.pts.model.vo;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.entity.ProjectEntity;
import lombok.Data;

import java.util.List;

/**
 * 接口表数据
 *
 * @author greyson
 * @since 2021/7/21
 */
@Data
public class InterfaceVo {
    private Integer interfaceId;
    private Integer projectId;
    private Integer messageTypeId;

    /**
     * 接口名称
     */
    private String interfaceName;

    private String messageType;


    /**
     * 消息类型描述
     */
    private String messageDescription;

    private List<ParamEntity> input;

    private List<ParamEntity> output;
    /**
     * 作为服务端还是客户端 前端传值
     */
    private String startMode;

}
