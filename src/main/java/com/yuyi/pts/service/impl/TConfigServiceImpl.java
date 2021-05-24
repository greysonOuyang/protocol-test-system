package com.yuyi.pts.service.impl;

import com.yuyi.pts.dao.TConfigDao;
import com.yuyi.pts.model.client.TConfig;
import com.yuyi.pts.service.TConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: wzl
 * @date:2021-05-21 10:54
 */
@Service
public class TConfigServiceImpl implements TConfigService {


    @Autowired
    private TConfigDao tConfigDao;

    @Override
    public int deleteByPrimaryKey(String configId) {
        return tConfigDao.deleteByPrimaryKey(configId);
    }

    @Override
    public int insert(TConfig record) {
        return tConfigDao.insert(record);
    }

    @Override
    public int insertSelective(TConfig record) {
        return tConfigDao.insertSelective(record);
    }

    @Override
    public TConfig selectByPrimaryKey(String configId) {
        return tConfigDao.selectByPrimaryKey(configId);
    }

    @Override
    public int updateByPrimaryKeySelective(TConfig record) {
        return tConfigDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TConfig record) {
        return tConfigDao.updateByPrimaryKey(record);
    }
}
