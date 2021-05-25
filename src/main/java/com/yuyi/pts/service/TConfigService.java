package com.yuyi.pts.service;


import com.yuyi.pts.model.client.TConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 向第三方接口发送消息的服务
 *
 * @author wzl
 * @since 2021/5/21
 */

public interface TConfigService {

    int deleteByPrimaryKey(Integer configId);

    boolean insert(Map<String, Object> map);

    int insertSelective(TConfig record);

    TConfig selectByPrimaryKey(Integer configId);

    int updateByPrimaryKeySelective(TConfig record);

    int updateByPrimaryKey(TConfig record);

    void deleteAll();

    void delConfigKeyId(String configId);
}
