package com.yuyi.pts.controller;

import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectWithMessageTypeEntity;
import com.yuyi.pts.repository.MessageTypeRepository;
import com.yuyi.pts.repository.ProjectWithMessageTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
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

    @Autowired
    ProjectWithMessageTypeRepository projectWithMessageTypeRepository;

    @GetMapping("/find/message/list")
    public List<ProjectWithMessageTypeEntity> findProjectList() {
        return projectWithMessageTypeRepository.findListGroupByMessageBelongId();
    }

    /**
     * 查询消息类型下拉选项
     * @param messageBelongId
     * @return
     */
    @GetMapping("/find/opt/list")
    public List<MessageTypeEntity> findMessageTypeList(String messageBelongId) {
        return messageTypeRepository.findListByMessageTypeId(messageBelongId);
    }

    @PostMapping("/save")
    public void saveMessageType(@RequestBody MessageTypeEntity messageTypeEntity) {
        messageTypeRepository.save(messageTypeEntity);
    }
}
