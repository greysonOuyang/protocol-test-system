package com.yuyi.pts.repository;

import com.yuyi.pts.entity.ParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
public interface ParamRepository extends JpaRepository<ParamEntity, Integer>, JpaSpecificationExecutor<ParamEntity> {
    @Query(value = "select * from t_param t where t.interface_id = :interfaceId and param_io = :paramIo", nativeQuery = true)
    List<ParamEntity> findBy(Integer interfaceId, String paramIo);
}
