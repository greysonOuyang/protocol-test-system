package com.yuyi.pts.service;

import com.yuyi.pts.model.vo.ProjectDto;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
public interface ProjectService {
    ProjectDto findBy(String interfaceId);
}
