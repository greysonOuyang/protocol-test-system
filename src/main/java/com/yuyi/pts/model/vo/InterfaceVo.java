package com.yuyi.pts.model.vo;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.ParamEntity;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
@Component
@Getter
public class InterfaceVo {
    private InterfaceEntity interfaceEntity;
    private String messageType;
    private List<ParamEntity> input;
    private List<ParamEntity> output;
}
