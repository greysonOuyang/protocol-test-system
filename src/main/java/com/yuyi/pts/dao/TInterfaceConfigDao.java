package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TInterfaceConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TInterfaceConfigDao {
    int deleteByPrimaryKey(String interfaceConfigId);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(String interfaceConfigId);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);

    List<TInterfaceConfig> selectByRequestType(String requestType);

    void deleteAll();
}