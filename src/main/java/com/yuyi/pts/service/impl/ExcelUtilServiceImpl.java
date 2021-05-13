package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.ParamConstant;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.DateTimeUtil;
import com.yuyi.pts.common.util.ExcelUtils;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
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
    @Override
    public void downLoadExcel(HttpServletResponse response, JSONObject object) throws IOException {
        // 获取到接口名称，根据接口名称去配置对应的excel文件
        String interfaceName = object.get("interfaceName").toString();
        if (ExcelConstant.PLAN_INFO.equals(interfaceName)) {
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
                return ResultEntity.failedWithMsg("无法获取到文件");
            }
            try (InputStream inputStream = file.getInputStream()) {
                ExcelLogs logs = new ExcelLogs();
                Map map = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
                List<Map> inputMapList = (List) map.get("Input");
                List<Param> input = new ArrayList<>();
                for (Map res : inputMapList) {
                    Param param = CommonUtil.mapToJavaBean(res, Param.class);
                    input.add(param);
                }
                ServiceInterface serviceInterface = new ServiceInterface();
                if (input != null && !input.isEmpty()) {
                    serviceInterface.setInput(input);
                }
                List<Map> outputMapList = (List) map.get("Output");
                List<Param> output = new ArrayList<>();

                for (Map res : outputMapList) {
                    Param param = CommonUtil.mapToJavaBean(res, Param.class);
                    output.add(param);
                }
                if (output != null && !output.isEmpty()) {
                    serviceInterface.setOutput(output);
                    System.out.println("serviceInterface 获取成功");
                }
                // TODO UUID不唯一
                serviceInterface.setInterfaceId(UUID.randomUUID().toString().replace("-", ""));
                String originalFilename = file.getOriginalFilename();
                int length = originalFilename.length();
                String fileName = originalFilename.substring(0, length - 5);
                serviceInterface.setInterfaceName(fileName);
                InterfaceCache.put(serviceInterface.getInterfaceId(), serviceInterface);
            } catch (IOException e) {
                log.error("读取Excel文件出错：{}", e.getMessage());
                return ResultEntity.failedWithMsg("解析文件出错");
            }
        }
        return ResultEntity.successWithNothing();
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
