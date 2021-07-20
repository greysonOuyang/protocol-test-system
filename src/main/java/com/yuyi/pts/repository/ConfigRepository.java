package com.yuyi.pts.repository;

import com.yuyi.pts.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, String>, JpaSpecificationExecutor<ConfigEntity> {
}
