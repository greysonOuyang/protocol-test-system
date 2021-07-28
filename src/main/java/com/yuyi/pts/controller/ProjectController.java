package com.yuyi.pts.controller;

import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.entity.ProjectWithMessageTypeEntity;
import com.yuyi.pts.model.vo.ProjectVo;
import com.yuyi.pts.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    ProjectCodecRepository projectCodecRepository;

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Autowired
    ProjectWithMessageTypeRepository projectWithMessageTypeRepository;

    @PostMapping("/save")
    public void saveProject(@RequestBody ProjectEntity projectEntity) {
        ProjectEntity save = projectRepository.save(projectEntity);
        ProjectWithMessageTypeEntity messageTypeEntity = projectWithMessageTypeRepository.findByBelongId(projectEntity.getMessageBelongId());
        messageTypeEntity.setProjectId(save.getProjectId());
        projectWithMessageTypeRepository.save(messageTypeEntity);
    }

    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    @PostMapping("/find/list")
    public List<ProjectVo> findProjectList() {
        List<ProjectVo> projectVoList = new ArrayList<>();
        ProjectVo projectDto = new ProjectVo();
        List<ProjectEntity> projectEntityList = projectRepository.findAll();
        projectEntityList.forEach((item) -> {
            projectDto.setProjectId(item.getProjectId());
            projectDto.setProjectName(item.getProjectName());
            projectDto.setMessageBelongId(item.getMessageBelongId());
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
