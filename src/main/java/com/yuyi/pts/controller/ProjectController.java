package com.yuyi.pts.controller;

import com.yuyi.pts.common.util.Desc;
import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.entity.ProjectWithCodecEntity;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.model.vo.ProjectVo;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.repository.ProjectCodecRepository;
import com.yuyi.pts.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    ProjectCodecRepository projectCodecRepository;

    @PostMapping("/save")
    public void saveProject(@RequestBody ProjectDto projectDto) {
        String messageTypeId = projectDto.getMessageTypeId();
        ProjectEntity projectEntity = projectDto.getProjectEntity();

        projectRepository.save(projectEntity);

//        ProjectEntity projectEntity = new ProjectEntity();
//        projectEntity.setProjectName(projectVo.getProjectName());
//        ProjectEntity project = projectRepository.save(projectEntity);
//        ProjectWithCodecEntity pc1 = new ProjectWithCodecEntity();
//        pc1.setProjectId(project.getProjectId());
//        pc1.setCodecId(projectVo.getEncoderId());
//        projectCodecRepository.save(pc1);
//        ProjectWithCodecEntity pc2 = new ProjectWithCodecEntity();
//        pc2.setProjectId(project.getProjectId());
//        pc2.setCodecId(projectVo.getDecoderId());
//        projectCodecRepository.save(pc2);
    }

    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    @PostMapping("/find/list")
    public List<ProjectVo> findProjectList() {
//        List<ProjectVo> projectList = projectRepository.findProjectList();
//        projectList.forEach((item) -> {
//            if (item.getCodecType().equals("1")) {
//                item.setEncoderDesc(item.getCodecDesc());
//            } else {
//                item.setDecoderDesc(item.getCodecDesc());
//            }
//        });
        List<ProjectVo> projectVoList = new ArrayList<>();
        ProjectVo projectDto = new ProjectVo();
        List<ProjectEntity> projectEntityList = projectRepository.findAll();
        projectEntityList.forEach((item) -> {
            projectDto.setProjectName(item.getProjectName());
            Optional<CodecEntity> encoder = codecRepository.findById(item.getEncoderId());
            if (encoder.isPresent()) {
                projectDto.setEncoderDesc(encoder.get().getCodecDesc());
            }
            Optional<CodecEntity> decoder= codecRepository.findById(item.getDecoderId());
            if (decoder.isPresent()) {
                projectDto.setDecoderDesc(decoder.get().getCodecDesc());
            }
            projectVoList.add(projectDto);
        });
        return projectVoList;
    }
}
