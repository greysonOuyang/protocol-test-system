package com.yuyi.pts.controller;


import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.RequestEntity;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.repository.InterfaceRepository;
import com.yuyi.pts.repository.RequestRepository;
import com.yuyi.pts.service.impl.ParamServiceImpl;
import com.yuyi.pts.service.impl.TConfigServiceImpl;
import com.yuyi.pts.service.impl.TInterfaceConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("interface/findAll")
    public List<ServiceInterface> findAllInterface() {
        List<ServiceInterface> serviceInterfaceList = new ArrayList<>();
        InterfaceCache.INTERFACE_MAP.forEach((k, v) -> {
            serviceInterfaceList.add(v);
        });
        return serviceInterfaceList;
    }

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
     * 服务端增加
     *
     * @param interfaceEntity
     * @return
     */
    @PostMapping("/save")
    public String addServerInterface(@RequestBody InterfaceEntity interfaceEntity) {
        interfaceRepository.save(interfaceEntity);
        return ResultUtil.successWithNothing();
    }

}
