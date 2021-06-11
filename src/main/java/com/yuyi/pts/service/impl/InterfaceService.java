package com.yuyi.pts.service.impl;

import com.yuyi.pts.dao.ParamDao;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.TInterfaceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/5/31
 */
@Service
public class InterfaceService {
    @Autowired
    TInterfaceConfigDao interfaceConfigDao;

    @Autowired
    ParamDao paramDao;

    public TInterfaceConfig selectInterfaceById(String serviceInterfaceId) {
        TInterfaceConfig tInterfaceConfig = interfaceConfigDao.selectByInfaceConfigId(serviceInterfaceId);
        List<Param> outPut = paramDao.findOutPut(serviceInterfaceId);
        tInterfaceConfig.setOutput(outPut);
        return tInterfaceConfig;
    }
}
