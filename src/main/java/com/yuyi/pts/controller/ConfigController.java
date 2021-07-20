package com.yuyi.pts.controller;

import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.entity.ConfigEntity;
import com.yuyi.pts.repository.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param map
     * @return
     */
    @PostMapping("/save")
    public String saveRequestConfig(@RequestBody Map<String, Object> map) {
        String id = (String) map.get("id");
        List<ConfigEntity> configList = (ArrayList) map.get("configList");
        configList.forEach(
                item-> {
                    item.setRequestId(id);
                }
        );
        configRepository.saveAll(configList);
        return ResultUtil.successWithNothing();
    }

}
