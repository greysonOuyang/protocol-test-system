package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.ParamConstant;
import com.yuyi.pts.common.util.CommonUtil;
import com.yuyi.pts.common.util.DateTimeUtil;
import com.yuyi.pts.common.util.ExcelUtils;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.dao.ParamDao;
import com.yuyi.pts.dao.TInterfaceConfigDao;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.client.Param;
import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TInterfaceConfigDao tInterfaceConfigDao;
    @Autowired
    private ParamDao paramDao;

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
              String tInterfaceConfigId = UUID.randomUUID().toString().replace("-", "");
                ExcelLogs logs = new ExcelLogs();
                Map map = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
                ServiceInterfaceJDBC serviceInterface = new ServiceInterfaceJDBC();
                List<Map> messageType = (List)  map.get("messageType");
                for (Map res : messageType) {
                    String type = (String) res.get("消息类型");
                    serviceInterface.setInterfaceType(type);
                }
                List<Map> inputMapList = (List) map.get("Input");
                for (Map res : inputMapList) {
                    Param param = getParam(res);
                    param.setParamIo("input");
                    param.setParamInterfaceId(tInterfaceConfigId);
                    param.setParamKeyId(UUID.randomUUID().toString().replace("-",""));
                    paramDao.insert(param);

                }
                List<Map> outputMapList = (List) map.get("Output");
                for (Map res : outputMapList) {
                    Param param = getParam(res);
                    param.setParamIo("output");
                    param.setParamInterfaceId(tInterfaceConfigId);
                    param.setParamKeyId(UUID.randomUUID().toString().replace("-",""));
                    paramDao.insert(param);

                }
                // TODO UUID不唯一
                serviceInterface.setInterfaceId(UUID.randomUUID().toString().replace("-", ""));
                String originalFilename = file.getOriginalFilename();
                TInterfaceConfig tInterfaceConfig = new TInterfaceConfig();
                tInterfaceConfig.setCurrentmode("server");
                // 文件名称作为 接口名称 //首先获取字符的位置
                int loc = originalFilename.indexOf(".xls");
                //再对字符串进行截取，获得想要得到的字符串
                String requestName = originalFilename.substring(0,loc);
                tInterfaceConfig.setRequestName(requestName);
                tInterfaceConfig.setInterfaceConfigId(tInterfaceConfigId);
                tInterfaceConfig.setRequestType(serviceInterface.getInterfaceType());
                tInterfaceConfigDao.insert(tInterfaceConfig);
            } catch (IOException e) {
                log.error("读取Excel文件出错：{}", e.getMessage());
                return ResultEntity.failedWithMsg("解析文件出错");
            }
        }
        return ResultEntity.successWithNothing();
    }

    private Param getParam(Map res) {
        Param param = new Param();
        if(res.get("value")!=null){
            param.setParamValue(res.get("value").toString());
        }
        if(res.get("length")!=null){
            param.setParamLength(Integer.parseInt(res.get("length").toString()));
        }
        if(res.get("index")!=null){
            param.setParamIndex(Integer.parseInt(res.get("index").toString()));
        }
        if(res.get("field")!=null){
            param.setParamField(res.get("field").toString());
        }
        if(res.get("type")!=null){
            param.setParamType(res.get("type").toString());
        }
        return param;
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
