package com.yuyi.pts.service;


import com.yuyi.pts.model.client.TConfig;
import org.springframework.stereotype.Service;

/**
 * 向第三方接口发送消息的服务
 *
 * @author wzl
 * @since 2021/5/21
 */

public interface TConfigService {

    int deleteByPrimaryKey(String configId);

    int insert(TConfig record);

    int insertSelective(TConfig record);

    TConfig selectByPrimaryKey(String configId);

    int updateByPrimaryKeySelective(TConfig record);

    int updateByPrimaryKey(TConfig record);
}
