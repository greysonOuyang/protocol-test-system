package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TConfigDao {
    int deleteByPrimaryKey(Integer configId);

    int insert(TConfig record);

    int insertSelective(TConfig record);

    TConfig selectByPrimaryKey(Integer configId);

    int updateByPrimaryKeySelective(TConfig record);

    int updateByPrimaryKey(TConfig record);

    List<TConfig> selectByInfaceConfigId(String interfaceConfigId);
    void deleteAll();
}