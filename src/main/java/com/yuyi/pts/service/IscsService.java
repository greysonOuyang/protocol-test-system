package com.yuyi.pts.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.yuyi.pts.common.util.FtpUtils;
import com.yuyi.pts.entity.StationEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/9/25
 */
@Service
public interface IscsService {

    void updateIscs(FtpUtils ft, StationEntity station, File file);

    void updateExcel(FtpUtils ft, StationEntity station, File file);

    /**
     * 重启综合监控
     *
     * @param ft
     * @param session
     */
    void restartIscs(FtpUtils ft, Session session);

    StationEntity findStation(String stationId);
}
