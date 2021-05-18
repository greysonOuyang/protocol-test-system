package com.yuyi.pts.controller;

import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ServerRequestDto;
import com.yuyi.pts.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Slf4j
@RestController
@RequestMapping("/main")
public class MainController {

    private final ScheduledExecutorService scheduledExecutorService = ScheduledThreadPoolUtil.getInstance();

    public static final Map<Integer, Boolean> STATUS_MAP = new HashMap<>();

    NettyServer nettyServer;

    @PostMapping("/start/server")
    public void execute(@RequestBody ServerRequestDto request) {
        String serviceInterfaceId = request.getInterfaceId();
        ServiceInterface serviceInterface = InterfaceCache.get(serviceInterfaceId);
        int port = request.getPort();
        nettyServer = new NettyServer(serviceInterface, port);
        nettyServer.start();
//        scheduledExecutorService.execute(() -> {
//        });
    }

    @GetMapping("/server/status")
    public boolean getServerStatus(String port) {
        return STATUS_MAP.get(Integer.parseInt(port));
    }

    /**
     * 关闭服务
     *
     * @return
     */
    @GetMapping("/stop/server")
    public boolean stopServer(String port) {
        STATUS_MAP.put(Integer.parseInt(port), false);
        return nettyServer.stop();
    }

    @PostMapping("/interface/add")
    public String addInterface(@RequestBody ServiceInterface serviceInterface) {
        String uuid = UUID.randomUUID().toString();
        int startNum = getCacheSize();
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

    @PostMapping("/param/save")
    public void saveParamData(@RequestBody ServiceInterface serviceInterface) {
        String interfaceId = serviceInterface.getInterfaceId();
        ServiceInterface service = InterfaceCache.get(interfaceId);
        if (serviceInterface.getInput() != null) {
            service.setInput(serviceInterface.getInput());
        }
        if (serviceInterface.getOutput() != null) {
            service.setOutput(serviceInterface.getOutput());
        }
        InterfaceCache.put(interfaceId, service);
    }

    /**
     * 获取到初始值
     * @return
     */
    public int getCacheSize(){
        return InterfaceCache.INTERFACE_MAP.size();
    }
}
