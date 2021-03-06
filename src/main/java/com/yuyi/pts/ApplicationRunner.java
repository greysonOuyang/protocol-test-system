package com.yuyi.pts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.Desc;
import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.MessageTypeEntity;
import com.yuyi.pts.entity.ProjectWithMessageTypeEntity;
import com.yuyi.pts.entity.StationEntity;
import com.yuyi.pts.netty.NettyServer;
import com.yuyi.pts.netty.initializer.TcpServerInitializer;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.repository.MessageTypeRepository;
import com.yuyi.pts.repository.ProjectWithMessageTypeRepository;
import com.yuyi.pts.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    @Autowired
    ProjectWithMessageTypeRepository projectWithMessageTypeRepository;

    @Autowired
    StationRepository stationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        initStationInfo();
        initCodec();
        initMessageType();
        TcpServerInitializer tcpServerInitializer = new TcpServerInitializer();
        NettyServer nettyServer = new NettyServer(tcpServerInitializer, 1998);
        nettyServer.start();
        log.info("???????????????Netty Server?????????");
    }

    private void initStationInfo() {
        String os = System.getProperty("os.name");
        String path = "/opt/station_code.json";
        if (os.toLowerCase().contains("windows")) {
            path = "d:/station_code.json";
        }
        InputStream inputStream = null;
        String stationJson = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("/station_list.json");
            if (Files.exists(Paths.get(path))) {
                inputStream = new FileInputStream(path);
            } else if (classPathResource.exists()) {
                path = classPathResource.getPath();
                inputStream = classPathResource.getInputStream();
            }
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stationJson = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray stationMapList = JSON.parseArray(stationJson);
        if (stationMapList != null) {
            for (Object jsonObject : stationMapList) {
                StationEntity stationIdModel = JSONObject.parseObject(jsonObject.toString(), StationEntity.class);
                stationRepository.save(stationIdModel);
            }
        }
    }

    /**
     * ????????????????????????????????????
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
     * ?????????????????????????????????
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

                    messageType.setMessageType((String) getType.invoke(obj));
                    messageType.setMessageDescription((String) getDescription.invoke(obj));

                    if (item.isAnnotationPresent(Desc.class)) {
                        Desc desc = (Desc) item.getDeclaredAnnotation(Desc.class);
                        messageType.setMessageBelongId(Integer.valueOf(desc.id()));
                        MessageTypeEntity save = messageTypeRepository.save(messageType);
                        ProjectWithMessageTypeEntity pmEntity = new ProjectWithMessageTypeEntity();
                        pmEntity.setMessageDesc(desc.value());
                        pmEntity.setMessageBelongId(Integer.valueOf(desc.id()));
                        projectWithMessageTypeRepository.save(pmEntity);
                    }

                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }));
    }
}
