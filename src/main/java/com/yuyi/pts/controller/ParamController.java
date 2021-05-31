package com.yuyi.pts.controller;



import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.enums.InterfaceMessageType;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.service.impl.ParamServiceImpl;
import com.yuyi.pts.service.impl.TInterfaceConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * description
 *
 * @author greyson
 * @since 2021/5/12
 */
@RestController
@RequestMapping("paramCtrl")
public class ParamController {
    @Autowired
   private ParamServiceImpl paramService;
    @Autowired
    private TInterfaceConfigServiceImpl interfaceConfigService;

    @PostMapping("/param/save")
    public void saveParamData(@RequestBody ServiceInterfaceJDBC serviceInterfaceJDBC) {
        paramService.insert(serviceInterfaceJDBC);
    }
    /**
     * 根据传入的消息类型,基本接口 从缓存中取出默认的参数列表，根据用户传过来的列车趟数，自动生成参数信息，对原有接口进行更新保存
     * @param map
     */
    @PostMapping("/planInfo/create")
    public String createParamInfo(@RequestBody Map<String, String> map) {
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
            ServiceInterface serviceInterface = InterfaceCache.get(interfaceId);
            String interfaceConfigId = UUID.randomUUID().toString().replace("-","");
            List<Param> paramList = paramService.selectByInterfaceConfigId(interfaceId);
            List<Param> terminalList = new ArrayList<>();
            Param param1 = new Param();
            param1.setParamField("站台数");
            param1.setParamValue(stationCount);
            param1.setParamType(FieldType.Int);
            param1.setParamLength(2);
            terminalList.add(param1);
            for (int i = 0; i < Integer.parseInt(stationCount); i++) {
                Param param2 = new Param();
                param2.setParamField("车站编号");
                int stationCode = Integer.parseInt(CommonUtil.random1To18IntStr());
                param2.setParamValue(String.valueOf(stationCode));
                param2.setParamType(FieldType.Int);
                param2.setParamLength(2);
                terminalList.add(param2);
                Param param3 = new Param();
                param3.setParamField("站台编号");
                String stationNumber = CommonUtil.random1To10IntStr();
                int index = Integer.parseInt(stationNumber);
                param3.setParamValue(stationNumber);
                param3.setParamType(FieldType.Int);
                param3.setParamLength(1);
                terminalList.add(param3);
                Param param4 = new Param();
                param4.setParamField("站台列车趟数");
                param4.setParamValue(trainCount);
                param4.setParamType(FieldType.Int);
                param4.setParamLength(2);
                terminalList.add(param4);
                // 获取模板配置
                List<Param> output = paramList.stream().filter(p->p.getParamIo().equals("output")).collect(Collectors.toList());
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

            ServiceInterfaceJDBC newServiceInterface = new ServiceInterfaceJDBC();
            newServiceInterface.setInterfaceType(interfaceType);
           // newServiceInterface.setInput(serviceInterface.getInput());

            newServiceInterface.setOutput(terminalList);
            terminalList.forEach(item->{
                item.setParamIo("output");
                item.setParamInterfaceId(interfaceConfigId);
                item.setParamKeyId(UUID.randomUUID().toString().replace("-",""));
                paramService.insert(item);
            });
            TInterfaceConfig tInterfaceConfig = new TInterfaceConfig();
            tInterfaceConfig.setInterfaceConfigId(interfaceConfigId);
            tInterfaceConfig.setCurrentmode("server");
            tInterfaceConfig.setRequestType(interfaceType);
            tInterfaceConfig.setRequestName(interfaceName);
            interfaceConfigService.insert(tInterfaceConfig);
        }
        return ResultEntity.successWithNothing();
    }
    public Param setValue(Param param, Map<String, String> map) {
        Param newParam = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (param.getParamField().equals(entry.getKey())) {
                param.setParamValue(entry.getValue());
                // paramFactory获取多例对象 单例存在以下bug：字段名相同，多轮循环返回的是同一个对象
                newParam = Param.paramFactory.get(param);;
                break;
            }
        }
        return newParam;
    }
}
