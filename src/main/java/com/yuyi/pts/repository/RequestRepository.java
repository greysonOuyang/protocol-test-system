package com.yuyi.pts.repository;

import com.yuyi.pts.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
public interface RequestRepository extends JpaRepository<RequestEntity, String>, JpaSpecificationExecutor<RequestEntity> {
    @Query(value = "delete from t_request t where t.request_type = requestType", nativeQuery = true)
    void deleteByRequestType(@Param("requestType") String requestType);
}
