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
@Getter
public class ProjectVo {
    private ProjectEntity projectEntity;
    private MessageTypeEntity messageTypeEntity;
}
