package com.yuyi.pts.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.yuyi.pts.common.util.FtpUtils;
import com.yuyi.pts.entity.StationEntity;
import com.yuyi.pts.model.vo.request.ContextRequestVo;
import com.yuyi.pts.model.vo.request.IscsRequest;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.initializer.AbstractNettyInitializer;
import com.yuyi.pts.netty.initializer.ModBusRequestInitializer;
import com.yuyi.pts.service.IscsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 综合监控服务
 *
 * @author greyson
 * @since 2021/9/25
 */
@Slf4j
@RequestMapping
@RestController("/iscs")
public class IscsController {

    @Autowired
    private IscsService iscsService;

    @Autowired
    private NettyClient nettyClient;

    private ModBusRequestInitializer initializer;


    /**
     * 更新综合监控
     *
     * @param request
     */
//    @MessageMapping("/update")
//    @SendTo("/topic/iscs")
    @PostMapping("/update/iscs")
    public void updateIscs(@RequestBody IscsRequest request) {
        List<String> stationIds = request.getStationIds();
        File file = new File(request.getJarPath());
        stationIds.forEach((stationId) -> {
            StationEntity station = iscsService.findStation(stationId);
            FtpUtils ft = new FtpUtils();
            iscsService.updateIscs(ft, station, file);
        });
    }

    /**
     * 更新点表
     *
     * @param request
     */
//    @MessageMapping("/update/excel")
//    @SendTo("/topic/excel")
    @PostMapping("/update/excel")
    public void updateExcel(@RequestBody IscsRequest request) {
        List<String> stationIds = request.getStationIds();
        File file = new File(request.getExcelPath());
        stationIds.forEach((stationId) -> {
            StationEntity station = iscsService.findStation(stationId);
            FtpUtils ft = new FtpUtils();
            iscsService.updateExcel(ft, station, file);
        });
    }

    @PostMapping("/restart")
    public void restartIscs(@RequestBody IscsRequest request) {
        List<String> stationIds = request.getStationIds();
        stationIds.forEach((stationId) -> {
            StationEntity station = iscsService.findStation(stationId);
            FtpUtils ft = new FtpUtils();
            Session session = ft.getSession(station.getIp(),
                    22, "root",
                    station.getPassword());
            iscsService.restartIscs(ft, session);
        });
    }

    @PostMapping("/execute/context")
    public void executeContext(@RequestBody ContextRequestVo requestVo) {
        // 中心
        if ("0".equals(requestVo.getInterfaceSelection())) {
            nettyClient.setHost("10.73.112.3");
            nettyClient.setPort(502);
            nettyClient.setAbstractNettyInitializer(initializer);
            nettyClient.start();
        } else {
            // 车站
            String stationId = requestVo.getStationId();
            StationEntity station = iscsService.findStation(stationId);
            nettyClient.setHost(station.getIp());
            nettyClient.setPort(502);
            nettyClient.setAbstractNettyInitializer(initializer);
            nettyClient.start();
        }
    }
}
