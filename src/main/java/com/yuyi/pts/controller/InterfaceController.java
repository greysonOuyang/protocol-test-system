package com.yuyi.pts.controller;


import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.repository.InterfaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口管理
 *
 * @author greyson
 * @since 2021/5/12
 */
@Slf4j
@RestController
@RequestMapping("interface")
public class InterfaceController {

    @Autowired
    InterfaceRepository interfaceRepository;

    @PostMapping("/del/list")
    public String deleteInterface(@RequestBody List<InterfaceEntity> interfaceEntityList) {
        interfaceRepository.deleteInBatch(interfaceEntityList);
        return ResultUtil.successWithNothing();
    }

    @PostMapping("/deleteAll")
    public void deleteAllInterface() {
        interfaceRepository.deleteAll();
    }

    @GetMapping("/find/list/by/projectId")
    public List<InterfaceEntity> getInterfaceList(String projectId) {
        return interfaceRepository.findByprojectId(projectId);
    }

    /**
     * 增加或修改接口
     *
     * @param interfaceEntity
     * @return
     */
    @PostMapping("/save")
    public String save(@RequestBody InterfaceEntity interfaceEntity) {
        interfaceRepository.save(interfaceEntity);
        return ResultUtil.successWithNothing();
    }

}
