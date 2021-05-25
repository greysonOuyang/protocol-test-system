package com.yuyi.pts.service;

import com.yuyi.pts.model.client.ClientInterface;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.vo.request.ClientInterfaceVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TInterfaceConfigService {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientInterfaceVO clientInterfaceVO);

    int insertSelective(TInterfaceConfig record);

    TInterfaceConfig selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(TInterfaceConfig record);

    int updateByPrimaryKey(TInterfaceConfig record);

    List<ClientInterface> selectByRequestType(String type);

    int deleteByInfaceConfigId(String keyId);

     void deleteAll();
}
