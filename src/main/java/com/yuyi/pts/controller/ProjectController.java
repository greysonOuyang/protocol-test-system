package com.yuyi.pts.controller;

import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.model.vo.ProjectVo;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CodecRepository codecRepository;

    @PostMapping("/save")
    public void saveProject(@RequestBody ProjectEntity projectEntity) {
        projectRepository.save(projectEntity);
    }

    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    @PostMapping("/find/list")
    public List<ProjectVo> findProjectList() {
        List<ProjectVo> pro
        ProjectDto projectDto = new ProjectDto();
        List<ProjectEntity> projectEntityList = projectRepository.findAll();
        projectEntityList.forEach((item) -> {
            Optional<CodecEntity> encoder = codecRepository.findById(item.getEncoderId());
            if (encoder.isPresent()) {
                projectDto.setEncoderType(encoder.get().getCodecName());
                projectDto.setEncoderType(encoder.get().getCodecName());
            }
        });
        return ;
    }
}
