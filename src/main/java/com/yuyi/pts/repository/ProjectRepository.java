package com.yuyi.pts.repository;

import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.model.vo.ProjectVo;
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
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer>, JpaSpecificationExecutor<ProjectEntity> {
}
