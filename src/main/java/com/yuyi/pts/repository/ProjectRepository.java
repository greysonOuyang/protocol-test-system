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

    @Query(value = "select new com.yuyi.pts.model.vo.ProjectVo(p.*,c.*) from t_project p left join t_project_codec pc on p.project_id = pc.project_id left join t_codec c on c.codec_id = pc.codec_id;", nativeQuery = true)
    List<ProjectVo> findProjectList();
}
