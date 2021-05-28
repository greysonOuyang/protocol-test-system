package com.yuyi.pts.controller;


import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.enums.InterfaceMessageType;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.dao.ParamDao;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.ClientInterface;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ClientInterfaceVO;
import com.yuyi.pts.service.impl.ParamServiceImpl;
import com.yuyi.pts.service.impl.TConfigServiceImpl;
import com.yuyi.pts.service.impl.TInterfaceConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 接口管理
 *
 * @author greyson
 * @since 2021/5/12
 */
@Slf4j
@RestController
@RequestMapping("interfaceCtrl")
public class InterfaceController {

    @Autowired
    private TConfigServiceImpl configService;
    @Autowired
    private ParamServiceImpl paramService;
    @Autowired
    private TInterfaceConfigServiceImpl interfaceConfigService;

    @Autowired
    TInterfaceConfigDao tInterfaceConfigDao;

    @PostMapping("/interface/add")
    public String addInterface(@RequestBody ServiceInterface serviceInterface) {
        String uuid = UUID.randomUUID().toString();
        int startNum = getCacheSize();
        serviceInterface.setInterfaceId(uuid);
        InterfaceCache.put(uuid, serviceInterface);
        int endNum = InterfaceCache.INTERFACE_MAP.size();
        int number = endNum - startNum;
        if(endNum-startNum>=1){
            return ResultEntity.successWithData(number);
        }else {
            return ResultEntity.failedWithData("新增失败，请重新尝试");
        }
    }

    @GetMapping("interface/findAll")
    public List<ServiceInterface> findAllInterface() {
        List<ServiceInterface> serviceInterfaceList = new ArrayList<>();
        InterfaceCache.INTERFACE_MAP.forEach((k, v) -> {
            serviceInterfaceList.add(v);
        });
        return serviceInterfaceList;
    }

    @PostMapping("interface/delServerInterfaceConfig")
    public String deleteInterface(@RequestBody List<Map> idList) {
        idList.forEach(item->{
            interfaceConfigService.deleteByInfaceConfigId(item.get("interfaceConfigId").toString());
        });
        return ResultEntity.successWithNothing();

    }

    @PostMapping("interface/deleteAll")
    public String deleteAllInterface() {
        InterfaceCache.INTERFACE_MAP.clear();
        int num = InterfaceCache.INTERFACE_MAP.size();
        if(num>0){
            return ResultEntity.failedWithData("删除失败，请重新尝试");
        }else{
            return ResultEntity.successWithData("删除成功");
        }
    }

    @PostMapping("/request/config/save")
    public String saveRequestConfig(@RequestBody Map<String, Object> map) {
        boolean flag = configService.insert(map);
        if(flag){
            return ResultEntity.successWithNothing();
        }else {
            return ResultEntity.failedWithMsg("配置失败");
        }
    }


    /**
     * 获取到初始值
     * @return
     */
    public int getCacheSize(){
        return InterfaceCache.INTERFACE_MAP.size();
    }

    public Param setValue(Param param, Map<String, String> map) {
        Param newParam = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (param.getField().equals(entry.getKey())) {
                param.setValue(entry.getValue());
                // paramFactory获取多例对象 单例存在以下bug：字段名相同，多轮循环返回的是同一个对象
                newParam = Param.paramFactory.get(param);;
                break;
            }
        }
        return newParam;
    }

    /**
     * 新增or更新接口-客户端
     * @param clientInterfaceVO
     */
    @PostMapping("/interface/save")
    public void saveClientInterface(@RequestBody ClientInterfaceVO clientInterfaceVO) {
        interfaceConfigService.insert(clientInterfaceVO);
    }

    /**
     * 查询对应的接口信息
     * @param map
     * @return
     */
    @PostMapping ("/interface/getAllInterfaceInfo")
    public List<ClientInterface> getAllInterfaceInfo(@RequestBody Map map) {
        String interfaceType = map.get("interfaceType").toString();
        List<ClientInterface> list = interfaceConfigService.selectByRequestType(interfaceType);
        return list;

    }

    /**
     * 删除接口
     * @param idList
     * @return
     */
    @PostMapping("/interface/delInterfaceConfig")
    public String delInterface(@RequestBody List<Map> idList) {
        idList.forEach(item->{
            interfaceConfigService.deleteByInfaceConfigId(item.get("interfaceConfigId").toString());
        });
        return ResultEntity.successWithNothing();
    }

    /**
     * 删除配置
     * @param map
     * @return
     */
    @PostMapping("/interface/delConfig")
    public String delConfig(@RequestBody Map map) {
         configService.delConfigKeyId(map.get("id").toString());
        return ResultEntity.successWithNothing();
    }
    /**
     * 清空接口
     * @param
     * @return
     */
    @PostMapping("/interface/delAllInterfaceInfo")
    public String delAllInterfaceInfo(@RequestBody Map map) {
        String requestType = map.get("requestType").toString();
        String currentMode =   map.get("currentMode").toString();
        interfaceConfigService.deleteByRequestType(requestType);
        return ResultEntity.successWithNothing();
    }
    /**
     * 清空配置
     * @param
     * @return
     */
    @PostMapping("/interface/delAllConfig")
    public String delAllConfig() {
        configService.deleteAll();
        return ResultEntity.successWithNothing();
    }

    /**
     *   服务端增加
     * @param serviceInterfaceJDBC
     * @return
     */
    @PostMapping("/interface/server/add")
    public String addServerInterface(@RequestBody ServiceInterfaceJDBC serviceInterfaceJDBC) {
        interfaceConfigService.insertServer(serviceInterfaceJDBC);
        return ResultEntity.successWithNothing();
    }

    /**
     *  服务端减少
     * @param map
     * @return
     */
    @PostMapping ("/interface/getAllServerInterfaceInfo")
    public List<ServiceInterfaceJDBC> getAllServerInterfaceInfo(@RequestBody Map map) {
        String currentMode = map.get("currentMode").toString();
        List<ServiceInterfaceJDBC> list = interfaceConfigService.selectByCurrentMode(currentMode);
        return list;

    }


}
