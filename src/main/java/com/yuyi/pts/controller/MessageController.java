package com.yuyi.pts.controller;

import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.repository.MessageTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/20
 */
@Slf4j
@RestController
@RequestMapping("/message/type")
public class MessageController {

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @GetMapping("/find/list")
    public List<MessageTypeEntity> findMessageTypeList(String projectId) {
        return messageTypeRepository.findListByProjectId(projectId);
    }

    @PostMapping("/save")
    public void saveMessageType(@RequestBody MessageTypeEntity messageTypeEntity) {
        messageTypeRepository.save(messageTypeEntity);
    }
}
