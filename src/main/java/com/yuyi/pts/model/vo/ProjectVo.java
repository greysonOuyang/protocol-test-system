package com.yuyi.pts.model.vo;

import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectEntity;
import lombok.Data;
import lombok.Getter;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Data
public class ProjectVo {
    private String projectId;
    private String projectName;
    private String encoderDesc;
    private String decoderDesc;
}
