package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.ClientInterface;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.vo.request.ClientInterfaceVO;
import com.yuyi.pts.service.TInterfaceConfigService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @description:
 * @author: wzl
 * @date:2021-05-21 10:54
 */
@Service
public class TInterfaceConfigServiceImpl implements TInterfaceConfigService {
     @Autowired
     private TInterfaceConfigDao tInterfaceConfigDao;


    @Override
    public int deleteByPrimaryKey(String id) {
        return 0;
    }

    @Override
    public int insert(ClientInterfaceVO clientInterfaceVO) {
        RequestType requestType = clientInterfaceVO.getRequestType();
        TInterfaceConfig tInterfaceConfig = clientInterfaceVO.getTInterfaceConfig();
        tInterfaceConfig.setInterfaceConfigId(UUID.randomUUID().toString().replace("-",""));
        tInterfaceConfig.setRequestType(requestType.name());
        return tInterfaceConfigDao.insert(tInterfaceConfig);
    }

    @Override
    public int insertSelective(TInterfaceConfig record) {
        return tInterfaceConfigDao.insertSelective(record);
    }

    @Override
    public TInterfaceConfig selectByPrimaryKey(String id) {
        return tInterfaceConfigDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TInterfaceConfig record) {
        return tInterfaceConfigDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TInterfaceConfig record) {
        return tInterfaceConfigDao.updateByPrimaryKey(record);
    }

    @Override
    public TInterfaceConfig selectByRequestType(String type) {
        return tInterfaceConfigDao.selectByRequestType(type);
    }
}
