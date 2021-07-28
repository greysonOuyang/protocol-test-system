package com.yuyi.pts.repository;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Repository
public interface InterfaceRepository extends JpaRepository<InterfaceEntity, String>, JpaSpecificationExecutor<InterfaceEntity> {
    @Query(value = "select * from t_interface t where t.project_id = :projectId", nativeQuery = true)
    List<InterfaceEntity> findByprojectId(String projectId);
}
