package com.yuyi.pts.service.impl;

import com.yuyi.pts.dao.TConfigDao;
import com.yuyi.pts.model.client.TConfig;
import com.yuyi.pts.service.TConfigService;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public boolean insert( Map<String, Object> map) {
        AtomicBoolean flag = new AtomicBoolean(true);
        String id = (String) map.get("id");
        List<TConfig> configList = (ArrayList) map.get("configList");
        configList.forEach(item-> {
            item.setInterfaceConfigId(id);
            item.setConfigId(UUID.randomUUID().toString().replace("-",""));
            int i = tConfigDao.insert(item);
            if(i!=1){
                flag.set(false);
            }
        });
        return flag.get();
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
