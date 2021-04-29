package com.yuyi.pts.controller;

import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ServerRequestDto;
import com.yuyi.pts.netty.server.NettyServer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@RestController
@RequestMapping("/main")
public class MainController {

    public static Map<String, Param> PARAM_MAP = new HashMap<>();
    /**
     *
     *   获取到最开始的缓存容量大小
     */
    int startNum = InterfaceCache.INTERFACE_MAP.size();

    @PostMapping("/start/server")
    public void execute(ServerRequestDto request) {
        ServiceInterface serviceInterface = request.getServiceInterface();
        int port = request.getPort();
        NettyServer nettyServer = new NettyServer(serviceInterface, port);
        nettyServer.start();
    }

    @PostMapping("/interface/add")
    public String addInterface(@RequestBody ServiceInterface serviceInterface) {
       InterfaceCache.put(serviceInterface.getInterfaceId(), serviceInterface);
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

    @PostMapping("interface/delete")
    public String deleteInterface(@RequestBody List<Map> idList) {
        InterfaceCache.remove(idList);
        // 拿最开始的缓存数据 减去删除后的缓存数据
        int endNum = InterfaceCache.INTERFACE_MAP.size();
        int number = startNum - endNum;
        if(number>=1){
            return ResultEntity.successWithData(number);
        }else{
            return ResultEntity.failedWithData("删除失败，请重新尝试");
        }
    }

    @PostMapping("interface/deleteAll")
    public void deleteAllInterface() {
        InterfaceCache.INTERFACE_MAP.clear();
    }

}
