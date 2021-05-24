package com.yuyi.pts.service.impl;

import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.service.TInterfaceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int deleteByPrimaryKey(Integer id) {
        return tInterfaceConfigDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TInterfaceConfig record) {
        return tInterfaceConfigDao.insert(record);
    }

    @Override
    public int insertSelective(TInterfaceConfig record) {
        return tInterfaceConfigDao.insertSelective(record);
    }

    @Override
    public TInterfaceConfig selectByPrimaryKey(Integer id) {
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
}
