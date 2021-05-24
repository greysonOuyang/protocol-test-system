package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TInterfaceConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TInterfaceConfigDao {
    int deleteByPrimaryKey(Integer keyId);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(Integer keyId);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);

    List<TInterfaceConfig> selectByRequestType(String type);

    void deleteAll();
}