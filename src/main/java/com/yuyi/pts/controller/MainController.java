package com.yuyi.pts.controller;

import com.yuyi.pts.model.server.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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


    public static final Map<Integer, String> STATUS_MAP = new HashMap<>();


    @GetMapping("/server/status")
    public String getServerStatus(String port) {
        if (port != null) {
            if (STATUS_MAP.get(Integer.parseInt(port)) != null) {
                return STATUS_MAP.get(Integer.parseInt(port));
            } else {
                return "initializing";
            }
        }
        return null;
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

}
