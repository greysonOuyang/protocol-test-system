package com.yuyi.pts.service.impl;

import com.yuyi.pts.dao.ParamDao;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: wzl
 * @date:2021-05-25 18:15
 */
@Service
public class ParamServiceImpl implements ParamService {
    @Autowired
    private ParamDao paramDao;
    @Override
    public int deleteByPrimaryKey(Integer paramId) {
        return paramDao.deleteByPrimaryKey(paramId);
    }

    @Override
    public int insert(ServiceInterfaceJDBC serviceInterfaceJDBC) {

        serviceInterfaceJDBC.getInterfaceName();
        serviceInterfaceJDBC.getInterfaceType();
        String interfaceId = serviceInterfaceJDBC.getInterfaceId();
        String paramIO = serviceInterfaceJDBC.getParamIO();
        List<Param> list = new ArrayList<>();
           if("input".equals(paramIO)){
               list = serviceInterfaceJDBC.getInput();
           }else{
               list = serviceInterfaceJDBC.getOutput();
           }
           list.forEach(item->{
               if(item.getParamKeyId()!=null){
                   paramDao.updateByPrimaryKeySelective(item);
               }else{
                   Param param = new Param();
                   param.setParamIo(paramIO);
                   param.setParamInterfaceId(interfaceId);
                   param.setParamKeyId(UUID.randomUUID().toString().replace("-",""));
                   param.setField(item.getField());
                   param.setIndex(item.getIndex());
                   param.setType(item.getType());
                   param.setLength(item.getLength());
                   param.setValue(item.getValue());
                   paramDao.insert(param);
               }

           });
        return 0;
    }

    @Override
    public int insertSelective(Param record) {
        return paramDao.insertSelective(record);
    }

    @Override
    public Param selectByPrimaryKey(Integer paramId) {
        return paramDao.selectByPrimaryKey(paramId);
    }

    @Override
    public int updateByPrimaryKeySelective(Param record) {
        return paramDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Param record) {
        return paramDao.updateByPrimaryKey(record);
    }

    @Override
    public List<Param> selectByInterfaceConfigId(String interfaceId) {
        return paramDao.selectByInfaceConfigId(interfaceId);
    }

    @Override
    public void insert(Param item) {
        paramDao.insert(item);
    }
}
