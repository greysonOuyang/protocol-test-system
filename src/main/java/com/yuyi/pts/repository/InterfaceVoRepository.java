package com.yuyi.pts.repository;

import com.yuyi.pts.model.vo.InterfaceVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/28
 */
@Repository
public interface InterfaceVoRepository {
    List<InterfaceVo> findInterfaceVo(Predicate predicate);
}
