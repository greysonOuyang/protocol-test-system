package com.yuyi.pts.controller;

import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.entity.ConfigEntity;
import com.yuyi.pts.repository.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Slf4j
@RestController
@RequestMapping("config")
public class ConfigController {
    @Autowired
    ConfigRepository configRepository;

    /**
     * 新增或修改配置
     * @param configList
     * @return
     */
    @PostMapping("/save")
    public String saveRequestConfig(@RequestBody List<ConfigEntity> configList) {
        configRepository.saveAll(configList);
        return ResultUtil.successWithNothing();
    }

    @GetMapping("/find/list/requestId")
    public List<ConfigEntity> findList(Integer requestId) {
        return configRepository.findConfigEntitiesByRequestId(requestId);
    }

}
