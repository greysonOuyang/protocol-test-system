package com.yuyi.pts.repository;

import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectWithMessageTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author greyson
 */
@Repository
public interface ProjectWithMessageTypeRepository extends JpaRepository<ProjectWithMessageTypeEntity, String>, JpaSpecificationExecutor<ProjectWithMessageTypeEntity> {
    @Query(value = "select * from t_project_message_type where message_belong_id = :messageBelongId", nativeQuery = true)
    ProjectWithMessageTypeEntity findByBelongId(Integer messageBelongId);

    ProjectWithMessageTypeEntity findByMessageBelongId(String messageBelongId);


    @Query(value = "select * from t_project_message_type group by message_belong_id", nativeQuery = true)
    List<ProjectWithMessageTypeEntity> findListGroupByMessageBelongId();


}
