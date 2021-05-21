package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TInterfaceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Mapper
public interface TInterfaceConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);
}