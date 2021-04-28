package com.yuyi.pts.controller;

import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.request.ServerRequestDto;
import com.yuyi.pts.netty.server.NettyServer;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public void addInterface(@RequestBody ServiceInterface serviceInterface) {
        String id = UUID.randomUUID().toString();
        interfaceMap.put(id, serviceInterface);
    }

    @GetMapping("interface/findAll")
    public List<ServiceInterface> findAllInterface() {
        List<ServiceInterface> serviceInterfaceList = new ArrayList<>();
        interfaceMap.forEach((k, v) -> {
            serviceInterfaceList.add(v);
        });
        return serviceInterfaceList;
    }
}
