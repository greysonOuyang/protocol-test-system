package com.yuyi.pts.controller;


import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.enums.InterfaceMessageType;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.model.client.ClientInterface;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ClientInterfaceVO;
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
    private TInterfaceConfigServiceImpl interfaceConfigService;
    /**
     * 根据传入的消息类型,基本接口 从缓存中取出默认的参数列表，根据用户传过来的列车趟数，自动生成参数信息，对原有接口进行更新保存
     * @param map
     */
    @PostMapping("/planInfo/create")
    public void createParamInfo(@RequestBody Map<String, String> map) {
        // 消息类型
        String interfaceType = map.get("interfaceType");
        // 接口Id
        String interfaceId = map.get("interfaceId");
        // 站台数
        String stationCount = map.get("stationCount");
        // 接口名称
        String interfaceName = map.get("interfaceName");
        // 站台列车趟数
        String trainCount = map.get("trainCount");
        // TODO 先根据趟数组织站台列车数据，每站包含多趟列车数据；总数居又包含多个站
        if (interfaceType.equals(InterfaceMessageType.PLAN_INFO.getDescription())) {
            com.yuyi.pts.model.server.ServiceInterface serviceInterface = InterfaceCache.get(interfaceId);
            List<Param> terminalList = new ArrayList<>();
            Param param1 = new Param();
            param1.setField("站台数");
            param1.setValue(stationCount);
            param1.setType(FieldType.Int);
            param1.setLength(2);
            terminalList.add(param1);
            for (int i = 0; i < Integer.parseInt(stationCount); i++) {
                Param param2 = new Param();
                param2.setField("车站编号");
                int stationCode = Integer.parseInt(CommonUtil.random1To18IntStr());
                param2.setValue(String.valueOf(stationCode));
                param2.setType(FieldType.Int);
                param2.setLength(2);
                terminalList.add(param2);
                Param param3 = new Param();
                param3.setField("站台编号");
                String stationNumber = CommonUtil.random1To10IntStr();
                int index = Integer.parseInt(stationNumber);
                param3.setValue(stationNumber);
                param3.setType(FieldType.Int);
                param3.setLength(1);
                terminalList.add(param3);
                Param param4 = new Param();
                param4.setField("站台列车趟数");
                param4.setValue(trainCount);
                param4.setType(FieldType.Int);
                param4.setLength(2);
                terminalList.add(param4);
                // 获取模板配置
                List<Param> output = serviceInterface.getOutput();
                for (int j = 0; j < Integer.parseInt(trainCount); j++) {
                    ArrayList<Param> trainList = new ArrayList<>();
                    for (Param param : output) {
                        ArrayList<Integer> list = new ArrayList(Arrays.asList(0,1,2));
                        int randomIndex = new Random().nextInt(list.size());

                        ArrayList<Integer> list2 = new ArrayList(Arrays.asList(0,1));
                        int randomIndex2 = new Random().nextInt(list2.size());

                        Map<String, String> valueMap = new HashMap<>();
                        valueMap.put("车组号长度", "3");
                        valueMap.put("车组号", CommonUtil.randomInt256Str());
                        valueMap.put("车次号长度", "3");
                        valueMap.put("车次号", CommonUtil.randomInt256Str());
                        valueMap.put("停车类型", String.valueOf(list2.get(randomIndex2)));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
                        String time  = LocalDateTime.now().format(formatter);
                        valueMap.put("计划到站时间", time);
                        valueMap.put("计划离站时间", time);
                        valueMap.put("终到站", CommonUtil.random1To18IntStr());
                        valueMap.put("当前状态", String.valueOf(list.get(randomIndex)));
                        valueMap.put("是否扣车", String.valueOf(list2.get(randomIndex2)));
                        valueMap.put("预留字段", "1");
                        String expressTag = String.valueOf(list2.get(randomIndex2));
                        valueMap.put("大站快车标志", expressTag);
                        int nextStation = 0;
                        if (index % 2 == 0) {
                            nextStation = stationCode + 1;
                        } else {
                            nextStation = stationCode - 1;
                        }
                        valueMap.put("下一停车站站码", String.valueOf(nextStation));
                        valueMap.put("预留", "1");
                        Param newParam = setValue(param, valueMap);
                        if (newParam != null) {
                            trainList.add(newParam);
                        }
                    }
                    terminalList.addAll(trainList);
                }
            }

            ServiceInterface newServiceInterface = new ServiceInterface();
            newServiceInterface.setInterfaceType(interfaceType);
            newServiceInterface.setInput(serviceInterface.getInput());
            newServiceInterface.setOutput(terminalList);
            newServiceInterface.setInterfaceName(interfaceName);
            String newInterfaceId = UUID.randomUUID().toString();
            newServiceInterface.setInterfaceId(newInterfaceId);
            InterfaceCache.put(newInterfaceId, newServiceInterface);
        }
    }

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
