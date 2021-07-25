package com.yuyi.pts;

import com.yuyi.pts.common.util.Desc;
import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.netty.NettyServer;
import com.yuyi.pts.netty.initializer.TcpServerInitializer;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.repository.MessageTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initCodec();
        initMessageType();
        TcpServerInitializer tcpServerInitializer = new TcpServerInitializer();
        NettyServer nettyServer = new NettyServer(tcpServerInitializer, 1998);
        nettyServer.start();
        log.info("闸机状态的Netty Server启动了");
    }

    /**
     * 初始化编码解码器到数据库
     */
    private void initCodec() {
        List<CodecEntity> codecEntityList = new ArrayList<>();
        Set<Class> classSet = ReflectionUtil.findAllClassesUsingClassLoader("com.yuyi.pts.netty.codec");
        classSet.forEach((item -> {
            CodecEntity codecEntity = new CodecEntity();
            codecEntity.setCodecName(item.getName().substring(25, item.getName().length()));
            if (item.getName().contains("Encoder")) {
                codecEntity.setCodecType("1");
            } else if (item.getName().contains("Decoder")) {
                codecEntity.setCodecType("0");
            }
            if (item.isAnnotationPresent(Desc.class)) {
                Desc desc = (Desc) item.getDeclaredAnnotation(Desc.class);
                codecEntity.setCodecDesc(desc.value());
            }
            codecEntity.setCodecName(item.getName());
            codecEntityList.add(codecEntity);
        }));
        codecRepository.saveAll(codecEntityList);
    }

    /**
     * 初始化消息类型到数据库
     */
    private void initMessageType() {
        Set<Class> classSet = ReflectionUtil.findAllClassesUsingClassLoader("com.yuyi.pts.common.enums.message.type");
        classSet.forEach((item -> {
            Object[] enums = item.getEnumConstants();
            try {
                Method getType = item.getMethod("getType");
                Method getDescription = item.getMethod("getDescription");
                for (Object obj: enums) {
                    MessageTypeEntity messageType = new MessageTypeEntity();
                    if (item.isAnnotationPresent(Desc.class)) {
                        Desc desc = (Desc) item.getDeclaredAnnotation(Desc.class);
                        messageType.setProjectName(desc.value());
                        messageType.setProjectId(Integer.valueOf(desc.id()) );
                    }
                    messageType.setMessageType((String) getType.invoke(obj));
                    messageType.setMessageDescription((String) getDescription.invoke(obj));
                    messageTypeRepository.save(messageType);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }));
    }
}
