package com.yuyi.pts.model.vo;

import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.entity.ProjectEntity;
import lombok.Data;

import java.util.List;

/**
 * Netty传参VO
 *
 * @author greyson
 * @since 2021/7/21
 */
@Data
public class ProjectDto {
    private ProjectEntity projectEntity;
    private List<ParamEntity> input;
    private List<ParamEntity> output;
    MessageTypeEntity messageTypeEntity;
    private String mode;
    /**
     * 编码器
     */
    private Integer encoderId;
    /**
     * 解码器
     */
    private Integer decoderId;
    private String encoderType;
    private String decoderType;
}
