package com.yuyi.pts.repository;

import com.yuyi.pts.entity.SelectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
public interface SelectionRepository extends JpaRepository<SelectionEntity, String>, JpaSpecificationExecutor<SelectionEntity> {
}
