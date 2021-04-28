package com.yuyi.pts.controller;

import com.yuyi.pts.common.cache.InterfaceCache;
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

    public static Map<String, ServiceInterface> interfaceMap = new HashMap<>();


    @PostMapping("/start/server")
    public void execute(ServerRequestDto request) {
        ServiceInterface serviceInterface = request.getServiceInterface();
        int port = request.getPort();
        NettyServer nettyServer = new NettyServer(serviceInterface, port);
        nettyServer.start();
    }

    @PostMapping("/interface/add")
    public String addInterface(@RequestBody ServiceInterface serviceInterface) {
       return InterfaceCache.put(serviceInterface.getInterfaceId(), serviceInterface);
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
    public void deleteInterface(@RequestBody String[] idList) {
        for (String id : idList) {
            InterfaceCache.remove(id);
        }

    }
}
