package com.yuyi.pts.repository;

import com.yuyi.pts.entity.ParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
public interface ParamRepository extends JpaRepository<ParamEntity, String>, JpaSpecificationExecutor<ParamEntity> {
}
