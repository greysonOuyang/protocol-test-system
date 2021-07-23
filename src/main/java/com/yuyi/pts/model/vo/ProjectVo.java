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
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("project_name")
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

    @JsonProperty("codec_id")
    private Integer codecId;
    @JsonProperty("codec_type")
    private String codecType;
    @JsonProperty("codec_desc")
    private String codecDesc;
    @JsonProperty("codec_name")
    private String codecName;

    public ProjectVo(String projectId, String projectName, Integer codecId, String codecType, String codecDesc, String codecName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.codecId = codecId;
        this.codecType = codecType;
        this.codecDesc = codecDesc;
        this.codecName = codecName;
    }
}
