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
    public int deleteByPrimaryKey(Integer configId) {
        return tConfigDao.deleteByPrimaryKey(configId);
    }

    @Override
    public boolean insert( Map<String, Object> map) {
        AtomicBoolean flag = new AtomicBoolean(true);
        String id = (String) map.get("id");
        List<Map> configList = (ArrayList) map.get("configList");
        configList.forEach(item-> {
            TConfig tConfig = new TConfig();
            tConfig.setConfigKey(item.get("configKey").toString());
            tConfig.setInterfaceConfigId(id);
            tConfig.setConfigType(item.get("configType").toString());
            tConfig.setConfigName(item.get("configName").toString());

            int i = tConfigDao.insert(tConfig);
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
    public TConfig selectByPrimaryKey(int configId) {
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

    @Override
    public void deleteAll() {
        tConfigDao.deleteAll();
    }
}
