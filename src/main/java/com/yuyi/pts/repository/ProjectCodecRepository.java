package com.yuyi.pts.repository;

import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.ProjectWithCodecEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author greyson
 */
@Repository
public interface ProjectCodecRepository extends JpaRepository<ProjectWithCodecEntity, Integer>, JpaSpecificationExecutor<ProjectWithCodecEntity> {
}
