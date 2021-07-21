package com.yuyi.pts;

import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.netty.NettyServer;
import com.yuyi.pts.netty.initializer.TcpServerInitializer;
import com.yuyi.pts.repository.CodecRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/25
 */
@Slf4j
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Autowired
    CodecRepository codecRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initCodec();
        TcpServerInitializer tcpServerInitializer = new TcpServerInitializer();
        NettyServer nettyServer = new NettyServer(tcpServerInitializer, 1998);
        nettyServer.start();
        log.info("闸机状态的Netty Server启动了");
    }

    private void initCodec() {
        List<CodecEntity> codecEntityList = new ArrayList<>();
        Set<Class> classSet = ReflectionUtil.findAllClassesUsingClassLoader("com.yuyi.pts.netty.codec");
        classSet.forEach((item -> {
            CodecEntity codecEntity = new CodecEntity();
            codecEntity.setCodecName(item.getName());
            if (item.getName().contains("encode")) {
                codecEntity.setCodecType("1");
            } else if (item.getName().contains("decode")) {
                codecEntity.setCodecType("0");
            }
            codecEntity.setCodecName(item.getName());
            codecEntityList.add(codecEntity);
        }));
        codecRepository.saveAll(codecEntityList);
    }
}
