//package com.yuyi.pts.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.stereotype.Component;
//
///**
// * 拿到用户输入的ip、port，若不指定，取默认值
// *
// * @author greyson
// * @since 2021/4/11
// */
//
//@Configuration
//public class ProtocolConfig {
//
//    @Value("${user.protocol.ip}")
//    private String host;
//
//    @Value("${user.protocol.port}")
//    private int port;
//
//    /**
//     * 获取用户指定ip
//     * TODO
//     * @return
//     */
//    @Bean
//    public String getHost() {
//        return host;
//    }
//
//    /**
//     * 获取用户指定port
//     * TODO
//     * @return
//     */
//    @Bean
//    public int getPort() {
//        return port;
//    }
//
//}
