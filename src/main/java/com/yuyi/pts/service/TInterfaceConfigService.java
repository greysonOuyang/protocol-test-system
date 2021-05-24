package com.yuyi.pts.service;

import com.yuyi.pts.model.client.TInterfaceConfig;
import org.springframework.stereotype.Service;

@Service
public interface TInterfaceConfigService {
    int deleteByPrimaryKey(Integer id);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);
}
