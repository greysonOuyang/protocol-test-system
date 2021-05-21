package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.TConfig;
import org.springframework.stereotype.Repository;


@Repository
public interface TConfigDao {
    int deleteByPrimaryKey(String configId);

    int insert(TConfig record);

    int insertSelective(TConfig record);

    TConfig selectByPrimaryKey(String configId);

    int updateByPrimaryKeySelective(TConfig record);

    int updateByPrimaryKey(TConfig record);
}