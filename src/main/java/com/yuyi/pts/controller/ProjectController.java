package com.yuyi.pts.controller;

import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.ProjectVo;
import com.yuyi.pts.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/save")
    public void saveProject(@RequestBody ProjectEntity projectEntity) {
        projectRepository.save(projectEntity);
    }
}
