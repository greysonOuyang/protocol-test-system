package com.yuyi.pts.service.impl;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ParamEntity;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.repository.InterfaceRepository;
import com.yuyi.pts.repository.MessageTypeRepository;
import com.yuyi.pts.repository.ParamRepository;
import com.yuyi.pts.repository.ProjectRepository;
import com.yuyi.pts.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    InterfaceRepository interfaceRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Autowired
    ParamRepository paramRepository;

    @Override
    public ProjectDto findBy(String interfaceId) {
        ProjectDto projectDto = new ProjectDto();
        Optional<InterfaceEntity> byId = interfaceRepository.findById(interfaceId);
        if (byId.isPresent()) {
            InterfaceEntity interfaceEntity = byId.get();
            Integer projectId = interfaceEntity.getProjectId();
            ProjectEntity projectEntity = projectRepository.getOne(projectId);
            projectDto.setProjectEntity(projectEntity);
            String messageTypeId = interfaceEntity.getMessageTypeId();
            MessageTypeEntity messageTypeEntity = messageTypeRepository.getOne(messageTypeId);
            projectDto.setMessageTypeEntity(messageTypeEntity);
            List<ParamEntity> input = paramRepository.findBy(interfaceId, "input");
            projectDto.setInput(input);
            List<ParamEntity> output = paramRepository.findBy(interfaceId, "output");
            projectDto.setOutput(output);
        }
        return projectDto;
    }
}
