package com.yuyi.pts.service;

import com.yuyi.pts.model.vo.InterfaceVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/28
 */
@Service
public interface InterfaceService {

    List<InterfaceVo> findList(Integer projectId);
}
