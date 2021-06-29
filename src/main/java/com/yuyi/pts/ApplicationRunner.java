package com.yuyi.pts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/25
 */
@Slf4j
@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        TcpServerInitializer tcpServerInitializer = new TcpServerInitializer("ats_receive");
//        NettyServer nettyServer = new NettyServer(tcpServerInitializer, 1998);
//        nettyServer.start();
//        log.info("闸机状态的Netty Server启动了");
    }
}
