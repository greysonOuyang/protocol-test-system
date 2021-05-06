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
    NettyServer nettyServer;
    @PostMapping("/start/server")
    public void execute(@RequestBody ServerRequestDto request) {
        String serviceInterfaceId = request.getInterfaceId();
        ServiceInterface serviceInterface = InterfaceCache.get(serviceInterfaceId);
        int port = request.getPort();
        nettyServer = new NettyServer(serviceInterface, port);
        nettyServer.start();
    }

    @GetMapping("/stop/server")
    public boolean stopServer() {
        return nettyServer.stop();
    }

    @PostMapping("/interface/add")
    public String addInterface(@RequestBody ServiceInterface serviceInterface) {
        int startNum = getCacheSize();
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

        int startNum = getCacheSize();
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
    public String deleteAllInterface() {
        InterfaceCache.INTERFACE_MAP.clear();
        int num = InterfaceCache.INTERFACE_MAP.size();
        if(num>0){
            return ResultEntity.failedWithData("删除失败，请重新尝试");
        }else{
            return ResultEntity.successWithData("删除成功");
        }
    }

    /**
     * 获取到初始值
     * @return
     */
    public int getCacheSize(){
        return InterfaceCache.INTERFACE_MAP.size();
    }
}
