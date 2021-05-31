package com.yuyi.pts.dao;

import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.client.TConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParamDao {
    int deleteByPrimaryKey(Integer paramId);

    int insert(Param record);

    int insertSelective(Param record);

    Param selectByPrimaryKey(Integer paramId);

    int updateByPrimaryKeySelective(Param record);

    int updateByPrimaryKey(Param record);

    List<Param> selectByInfaceConfigId(String interfaceConfigId);

    List<Param> findOutPut(String interfaceConfigId);

    void delByInterfaceConfigId(String keyId);

    List<ServiceInterfaceJDBC> selectTest(String interfaceConfigId);

}