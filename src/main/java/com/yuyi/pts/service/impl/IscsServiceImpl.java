package com.yuyi.pts.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.yuyi.pts.common.util.FtpUtils;
import com.yuyi.pts.entity.StationEntity;
import com.yuyi.pts.repository.StationRepository;
import com.yuyi.pts.service.IscsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 综合监控服务
 *
 * @author greyson
 * @since 2021/9/25
 */
@Slf4j
@Service
public class IscsServiceImpl implements IscsService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public void updateIscs(FtpUtils ft, StationEntity station, File file) {
        Session session = ft.getSession(station.getIp(),
                22, "root",
                station.getPassword());
        ChannelSftp sftp = ft.getSftp(session);
        String fileName = "iscs-interface-server.jar";
        log.info("开始上传到：[{}]", station.getIp());
        String result = ft.uploadFile(sftp, "/root/integrated_monitor/server",
                file, fileName);
        log.info("[{}]的上传结果：[{}]", station.getIp(), result);
        if (result.contains("上传成功")) {
            restartIscs(ft, session);
        }
        ft.closeAll(sftp, session);
    }

    @Override
    public void updateExcel(FtpUtils ft, StationEntity station, File file) {
        Session session = ft.getSession(station.getIp(),
                22, "root",
                station.getPassword());
        ChannelSftp sftp = ft.getSftp(session);
        log.info("开始上传到：[{}]", station.getIp());
        String result = ft.uploadFile(sftp,"/opt", file, "playctrl_map_code.xlsx");
        log.info("[{}]的上传结果：[{}]", station.getIp(), result);
        if (result.contains("上传成功")) {
            restartIscs(ft, session);
        }
        ft.closeAll(sftp, session);
    }

    /**
     * 重启综合监控
     *
     * @param ft
     * @param session
     */
    @Override
    public void restartIscs(FtpUtils ft, Session session) {
        log.info("开始重启综合监控程序");
        ft.exec(session, "sh /root/integrated_monitor/server/shutdown.sh");
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ft.exec(session, "sh /root/integrated_monitor/server/startup.sh");
    }

    @Override
    public StationEntity findStation(String stationId) {
        return stationRepository.findStationEntityByStationId(stationId);
    }
}
