package com.yuyi.pts.service;

import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;

import java.util.List;

public interface ParamService {
    int deleteByPrimaryKey(Integer paramId);

    int insert(ServiceInterfaceJDBC serviceInterfaceJDBC);

    int insertSelective(Param record);

    Param selectByPrimaryKey(Integer paramId);

    int updateByPrimaryKeySelective(Param record);

    int updateByPrimaryKey(Param record);

    List<Param> selectByInterfaceConfigId(String interfaceId);

    void insert(Param item);
}
