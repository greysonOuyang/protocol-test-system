package com.yuyi.pts.controller;

import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.util.ScheduledThreadPoolUtil;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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

    public static final Map<Integer, String> STATUS_MAP = new HashMap<>();

    NettyServer nettyServer;

//    @PostMapping("/start/server")
//    public void execute(@RequestBody ServerRequestDto request) {
//        String serviceInterfaceId = request.getInterfaceId();
//        ServiceInterface serviceInterface = InterfaceCache.get(serviceInterfaceId);
//        int port = request.getPort();
//        nettyServer = new NettyServer(serviceInterface, port);
//        nettyServer.start();
//
//    }

    @GetMapping("/server/status")
    public String getServerStatus(String port) {
        if (STATUS_MAP.get(Integer.parseInt(port)) != null) {
            return STATUS_MAP.get(Integer.parseInt(port));
        } else {
            return "initializing";
        }
    }

    /**
     * 关闭服务
     *
     * @return
     */
    @GetMapping("/stop/server")
    public boolean stopServer(String port) {
        STATUS_MAP.put(Integer.parseInt(port), "initializing");
        return WebsocketServerController.nettyServer.stop();
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


}
