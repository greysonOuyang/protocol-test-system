package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TInterfaceConfig;

public interface TInterfaceConfigDao {
    int deleteByPrimaryKey(String interfaceConfigId);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(String interfaceConfigId);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);

    TInterfaceConfig selectByRequestType(String requestType);
}