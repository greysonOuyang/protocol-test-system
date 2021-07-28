package com.yuyi.pts.service.impl;

import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.repository.InterfaceRepository;
import com.yuyi.pts.repository.MessageTypeRepository;
import com.yuyi.pts.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/28
 */
@Service
public class InterfaceServiceImpl implements InterfaceService {

    @Autowired
    InterfaceRepository interfaceRepository;

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Override
    public List<InterfaceVo> findList(Integer projectId) {
        List<InterfaceVo> interfaceVoList = new ArrayList<>();
        List<InterfaceEntity> interfaceEntities = interfaceRepository.findByprojectId(projectId);
        interfaceEntities.forEach((item) -> {
            InterfaceVo interfaceVo = new InterfaceVo();
            interfaceVo.setInterfaceId(item.getInterfaceId());
            interfaceVo.setInterfaceName(item.getInterfaceName());
            MessageTypeEntity message = messageTypeRepository.findMessageTypeEntityByMessageTypeId(item.getMessageTypeId());
            interfaceVo.setMessageType(message.getMessageType());
            interfaceVo.setMessageDescription(message.getMessageDescription());
            interfaceVoList.add(interfaceVo);
        });
        return interfaceVoList;
    }
}
