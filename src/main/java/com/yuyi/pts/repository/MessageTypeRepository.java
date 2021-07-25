package com.yuyi.pts.repository;

import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author greyson
 */
@Repository
public interface MessageTypeRepository extends JpaRepository<MessageTypeEntity, String>, JpaSpecificationExecutor<MessageTypeEntity> {
    @Query(value = "select * from t_message_type t where t.project_id = :projectId", nativeQuery = true)
    List<MessageTypeEntity> findListByProjectId(String projectId);

    @Query(value = "select DISTINCT t.project_id, t.message_id, t.project_name,t.message_type,t.message_description from t_message_type t group by t.project_id", nativeQuery = true)
    List<MessageTypeEntity> findProjectList();
}
