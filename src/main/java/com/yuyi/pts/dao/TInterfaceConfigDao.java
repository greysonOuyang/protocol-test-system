package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TInterfaceConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface TInterfaceConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TInterfaceConfig record);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);
}