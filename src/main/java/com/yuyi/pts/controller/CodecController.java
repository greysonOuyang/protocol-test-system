package com.yuyi.pts.controller;

import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.repository.CodecRepository;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Slf4j
@RestController
@RequestMapping("codec")
public class CodecController {

    @Autowired
    CodecRepository codecRepository;

    @GetMapping("/find/list/by/type")
    public List<CodecEntity> findCodecByType(String type) {
       return codecRepository.findListByType(type);
    }
}
