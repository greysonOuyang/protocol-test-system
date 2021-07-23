package com.yuyi.pts.repository;

import com.yuyi.pts.entity.CodecEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author greyson
 */
@Repository
public interface CodecRepository extends JpaRepository<CodecEntity, String>, JpaSpecificationExecutor<CodecEntity> {
    @Query(value = "select * from t_codec t where t.codec_type = :type", nativeQuery = true)
    List<CodecEntity> findListByType(@Param("type") String type);
}
