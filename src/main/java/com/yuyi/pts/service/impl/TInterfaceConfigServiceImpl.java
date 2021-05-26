package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.dao.ParamDao;
import com.yuyi.pts.dao.TConfigDao;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.*;
import com.yuyi.pts.model.vo.request.ClientInterfaceVO;
import com.yuyi.pts.service.TInterfaceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: wzl
 * @date:2021-05-21 10:54
 */
@Service
public class TInterfaceConfigServiceImpl implements TInterfaceConfigService {

    @Autowired
    private TInterfaceConfigDao tInterfaceConfigDao;
    @Autowired
    private TConfigDao configDao;
    @Autowired
    private ParamDao paramDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return tInterfaceConfigDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ClientInterfaceVO clientInterfaceVO) {
        RequestType requestType = clientInterfaceVO.getRequestType();
        TInterfaceConfig tInterfaceConfig = getTInterfaceConfig(clientInterfaceVO);
        tInterfaceConfig.setInterfaceConfigId(UUID.randomUUID().toString().replace("-", ""));
        tInterfaceConfig.setRequestType(requestType.name());
        return tInterfaceConfigDao.insert(tInterfaceConfig);
    }

    @Override
    public int insertSelective(TInterfaceConfig record) {
        return tInterfaceConfigDao.insertSelective(record);
    }

    @Override
    public TInterfaceConfig selectByPrimaryKey(int id) {
        return tInterfaceConfigDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TInterfaceConfig record) {
        return tInterfaceConfigDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TInterfaceConfig record) {
        return tInterfaceConfigDao.updateByPrimaryKey(record);
    }

    @Override
    public List<ClientInterface> selectByRequestType(String type) {
        List<TInterfaceConfig> listTInterfaceConfig = tInterfaceConfigDao.selectByRequestType(type);

        List<ClientInterface> clientInterfaceList = new ArrayList<>();
        listTInterfaceConfig.forEach(item -> {
            String id = item.getInterfaceConfigId();
            ClientInterface clientInterface = new ClientInterface();
            clientInterface.setConfigList(configDao.selectByInfaceConfigId(id));
            clientInterface.setRequestType(item.getRequestType());
            clientInterface.setContent(item.getContent());
            clientInterface.setRequestMethod(item.getRequestMethod());
            clientInterface.setRequestName(item.getRequestName());
            clientInterface.setPort(item.getPort());
            clientInterface.setUrl(item.getUrl());
            clientInterface.setId(item.getInterfaceConfigId());
            clientInterfaceList.add(clientInterface);
        });
        return clientInterfaceList;
    }

    @Override
    public int deleteByInfaceConfigId(String keyId) {
        // 删除配置 和 接口
        List<Param> tConfigList = paramDao.selectByInfaceConfigId(keyId);
        if(tConfigList.size()!=0){
            paramDao.delByInterfaceConfigId(keyId);
        }
        return tInterfaceConfigDao.deleteByInfaceConfigId(keyId);
    }

    @Override
    public int deleteByRequestType(String requestType) {
        List<TInterfaceConfig> list = tInterfaceConfigDao.selectByRequestType(requestType);
        list.forEach(item->{
            String interfaceConfigId = item.getInterfaceConfigId();
            configDao.delByInterfaceConfigId(interfaceConfigId);
        });
        tInterfaceConfigDao.deleteByRequestType(requestType);
        return 0;
    }

    @Override
    public void deleteAll() {
        tInterfaceConfigDao.deleteAll();
    }

    @Override
    public List<ServiceInterfaceJDBC> selectByCurrentMode(String currentMode) {
        List<TInterfaceConfig> list = tInterfaceConfigDao.selectByCurrentMode(currentMode);
        List<ServiceInterfaceJDBC> listResult = new ArrayList<>();
        list.forEach(item->{
            ServiceInterfaceJDBC serviceInterfaceJDBC = new ServiceInterfaceJDBC();
            String id = item.getInterfaceConfigId();
            serviceInterfaceJDBC.setInterfaceId(id);
            serviceInterfaceJDBC.setInterfaceName(item.getRequestName());
            serviceInterfaceJDBC.setInterfaceType(item.getRequestType());
            serviceInterfaceJDBC.setCurrentMode(item.getCurrentmode());
            List<Param> paramInput = paramDao.selectByInfaceConfigId(id).stream().filter(p->p.getParamIo().equals("input")).collect(Collectors.toList());
            List<Param> paramOutput = paramDao.selectByInfaceConfigId(id).stream().filter(p->p.getParamIo().equals("output")).collect(Collectors.toList());
            //  输入参数 和 输出 分开保存
            if(paramInput.size()>0){
                serviceInterfaceJDBC.setInput(paramInput);
            }
            if(paramOutput.size()>0){
                serviceInterfaceJDBC.setOutput(paramOutput);
            }
            listResult.add(serviceInterfaceJDBC);

        });
        return listResult;
    }

    private FieldType getParamType(String paramType) {
        switch (paramType){
            case "Hex":
                return FieldType.Hex;
            case "String":
                return FieldType.String;
            case "ASCII":
                return FieldType.ASCII;
            case "Time":
                return FieldType.Time;
            case "Int":
                return FieldType.Int;
            case "null":
                return FieldType.Unkonw;
            default:
                return null;
        }
    }

    /**
     * 获取到TInterfaceConfig实体类
     * @param clientInterfaceVO
     * @return
     */
    TInterfaceConfig getTInterfaceConfig(ClientInterfaceVO clientInterfaceVO){
        ClientInterface clientInterface = clientInterfaceVO.getClientInterface();
        TInterfaceConfig interfaceConfig = new TInterfaceConfig();
        interfaceConfig.setContent(clientInterface.getContent());;
        interfaceConfig.setRequestType(clientInterface.getRequestType());;
        interfaceConfig.setPort(clientInterface.getPort());;
        interfaceConfig.setRequestMethod(clientInterface.getRequestMethod());;
        interfaceConfig.setRequestName(clientInterface.getRequestName());;
        interfaceConfig.setUrl(clientInterface.getUrl());
        return interfaceConfig;
    }

    @Override
    public void insertServer(ServiceInterfaceJDBC serviceInterfaceJDBC) {
        serviceInterfaceJDBC.getInterfaceName();
        TInterfaceConfig tInterfaceConfig = new TInterfaceConfig();
        tInterfaceConfig.setRequestType(serviceInterfaceJDBC.getInterfaceType());
        //  请求名称 与 接口名称进行匹配
        tInterfaceConfig.setRequestName(serviceInterfaceJDBC.getInterfaceName());
        tInterfaceConfig.setCurrentmode(serviceInterfaceJDBC.getCurrentMode());
        tInterfaceConfig.setInterfaceConfigId(UUID.randomUUID().toString().replace("-",""));
        tInterfaceConfigDao.insert(tInterfaceConfig);
    }
}
