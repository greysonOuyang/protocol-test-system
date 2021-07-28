package com.yuyi.pts.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Data
@NoArgsConstructor
public class ProjectVo {
    private Integer projectId;

    private String projectName;
    /**
     * 编码器
     */
    private Integer encoderId;
    /**
     * 解码器
     */
    private Integer decoderId;
    private String encoderDesc;
    private String decoderDesc;

    private Integer codecId;
    private String codecType;
    private String codecDesc;
    private String codecName;

    private Integer messageBelongId;

}
