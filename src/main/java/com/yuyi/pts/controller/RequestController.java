package com.yuyi.pts.controller;

import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.entity.RequestEntity;
import com.yuyi.pts.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
@RequestMapping("/request")
public class RequestController {

    @Autowired
    RequestRepository requestRepository;

    /**
     * 新增or更新接口-客户端
     *
     * @param requestEntity
     */
    @PostMapping("/save")
    public void saveClientInterface(@RequestBody RequestEntity requestEntity) {
        requestRepository.save(requestEntity);
    }

    /**
     * 查询对应的接口信息
     *
     * @param requestType
     * @return
     */
    @GetMapping("/find/list/by/type")
    public List<RequestEntity> getAllInterfaceInfo(String requestType) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestType(requestType);
        Example<RequestEntity> example = Example.of(requestEntity);
        return requestRepository.findAll(example);
    }

    @GetMapping("find/by/id")
    public RequestEntity findById(Integer requestId) {
        Optional<RequestEntity> byId = requestRepository.findById(requestId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    /**
     * 删除接口
     *
     * @param requestEntityList
     * @return
     */
    @PostMapping("/del/list")
    public String delInterface(@RequestBody List<RequestEntity> requestEntityList) {
        requestRepository.deleteInBatch(requestEntityList);
        return ResultUtil.successWithNothing();
    }

    /**
     * 根据类型删除
     *
     * @param
     * @return
     */
    @GetMapping("/del/list/by/type")
    public String delAllInterfaceInfo(String requestType) {
        requestRepository.deleteByRequestType(requestType);
        return ResultUtil.successWithNothing();
    }
}
