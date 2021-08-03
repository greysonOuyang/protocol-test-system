package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.constant.ParamConstant;
import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.DateTimeUtil;
import com.yuyi.pts.common.util.ExcelUtils;
import com.yuyi.pts.common.util.ResultUtil;
import com.yuyi.pts.entity.*;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.repository.*;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author : wzl
 * @date : 2021/4/27/15:57
 * @description: excelUtil实现类
 */
@Service
@Slf4j
public class ExcelUtilServiceImpl implements ExcelUtilService {

    @Autowired
    ParamRepository paramRepository;

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectWithMessageTypeRepository pmRepository;

    @Autowired
    InterfaceRepository interfaceRepository;

    @Override
    public void downLoadExcel(HttpServletResponse response, JSONObject object) throws IOException {
        // 获取到接口名称，根据接口名称去配置对应的excel文件
        String interfaceName = object.get("interfaceName").toString();
        if (ConstantValue.PLAN_INFO.equals(interfaceName)) {
            //   给serviceInterfaceInput赋初始值 表格内容赋值
            ServiceInterface serviceInterfaceInput = ParamConstant.SERVICE_INTERFACE;
            Collection<Object> dataInput = new ArrayList<>();
            for (int i = 0; i < serviceInterfaceInput.getInput().size(); i++) {
                dataInput.add(serviceInterfaceInput.getInput().get(i));
            }
            Collection<Object> dataOutput = new ArrayList<>();
            for (int i = 0; i < serviceInterfaceInput.getOutput().size(); i++) {
                dataOutput.add(serviceInterfaceInput.getOutput().get(i));
            }
            Map mapList = new LinkedHashMap<>();
            mapList.put("sheel0", dataInput);
            mapList.put("sheel1", dataOutput);
            String date = DateTimeUtil.getStringDateShort();
            String fileName = interfaceName.toUpperCase() + "_" + date + ".xls";
            //  给表头加数据
            Map map = ParamConstant.getModel();
            ExcelUtil.exportExcel(map, mapList, response, fileName);
        }
    }

    @Override
    public String upLoadExcel(MultipartHttpServletRequest mRequest) throws IOException {
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            MultipartFile file = mRequest.getFile(key);
            if (file == null) {
                log.error("无法获取到文件");
                return ResultUtil.failedWithMsg("无法获取到文件");
            }
            try (InputStream inputStream = file.getInputStream()) {

                String originalFilename = file.getOriginalFilename();
                // 文件名称作为 接口名称 //首先获取字符的位置
                int loc = originalFilename.indexOf(".xls");
                //再对字符串进行截取，获得想要得到的字符串
                String interfaceName = originalFilename.substring(0,loc);
                InterfaceEntity interfaceEntity = new InterfaceEntity();
                interfaceEntity.setInterfaceName(interfaceName);

                InterfaceEntity interfaceSave = new InterfaceEntity();

                ExcelLogs logs = new ExcelLogs();
                Map map = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
                List<Map> attribute = (List)  map.get("Attribute");
                for (Map res : attribute) {
                    String type = (String) res.get("消息类型");
                    String projectName = (String) res.get("项目名称");
                    ProjectEntity project = projectRepository.findProjectEntitiesByProjectName(projectName);
                    if (project == null) {
                        return ResultUtil.failedWithMsg("解析文件出错，找不到项目");
                    }
                    interfaceEntity.setProjectId(project.getProjectId());
                    ProjectWithMessageTypeEntity pm = pmRepository.findProjectWithMessageTypeEntityByProjectId(project.getProjectId());
                    MessageTypeEntity messageTypeEntity = new MessageTypeEntity();
                    messageTypeEntity.setMessageBelongId(pm.getMessageBelongId());
                    messageTypeEntity.setMessageDescription(type);
                    Example<MessageTypeEntity> example = Example.of(messageTypeEntity);
                    Optional<MessageTypeEntity> one = messageTypeRepository.findOne(example);
                    if (one.isPresent()) {
                        interfaceEntity.setMessageTypeId(one.get().getMessageTypeId());
                    }
                    interfaceSave = interfaceRepository.save(interfaceEntity);
                }


                List<Map> inputMapList = (List) map.get("Input");
                for (Map res : inputMapList) {
                    ParamEntity param = CommonUtil.mapToJavaBean(res, ParamEntity.class);
                    param.setParamIo("input");
                    param.setInterfaceId(interfaceSave.getInterfaceId());
                    paramRepository.save(param);
                }
                List<Map> outputMapList = (List) map.get("Output");
                for (Map res : outputMapList) {
                    ParamEntity param = CommonUtil.mapToJavaBean(res, ParamEntity.class);
                    param.setParamIo("output");
                    param.setInterfaceId(interfaceSave.getInterfaceId());
                    paramRepository.save(param);

                }
            } catch (IOException e) {
                log.error("读取Excel文件出错：{}", e.getMessage());
                return ResultUtil.failedWithMsg("解析文件出错");
            }
        }
        return ResultUtil.successWithNothing();
    }


    @Override
    public boolean analysisFile(MultipartHttpServletRequest mreq) {
        List<Map> maps = null;
        try {
            maps = ExcelUtils.analysisFile(mreq);
        } catch (Exception e) {
            return false;
        }
        if (maps == null) {
            return false;
        } else {
            // 处理信息
            return true;
        }
    }
}
