package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.InterfaceCache;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.MapAndListConstant;
import com.yuyi.pts.common.constant.ParamConstant;
import com.yuyi.pts.common.util.DateTimeUtil;
import com.yuyi.pts.common.util.ExcelUtils;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;
import com.yuyi.pts.model.vo.response.PlanInfo;
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
 * @description:  excelUtil实现类
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
            // todo 模型设计中 输入参数
            for (int i = 0; i < serviceInterfaceInput.getInput().size(); i++) {
                dataInput.add(serviceInterfaceInput.getInput().get(i));
            }
            // todo  输出参数 暂未设计模型
            Collection<Object> dataOutput = new ArrayList<>();
            for (int i = 0; i < serviceInterfaceInput.getOutput().size(); i++) {
                dataOutput.add(serviceInterfaceInput.getOutput().get(i));
            }
            Map mapList = new LinkedHashMap<>();
            mapList.put("sheel0",dataInput);
            mapList.put("sheel1",dataOutput);
            String date = DateTimeUtil.getStringDateShort();
            String fileName = interfaceName.toUpperCase()+"_" + date +".xls";
            //  给表头加数据
            Map map = ParamConstant.getModel();
            ExcelUtil.exportExcel(map, mapList, response,fileName);
        }
    }

    @Override
    public boolean upLoadExcel(MultipartHttpServletRequest mRequest) throws IOException {
        ServiceInterface serviceInterface = new ServiceInterface();
        boolean flag = false;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            MultipartFile file = mRequest.getFile(key);
            if (file != null) {
                try (InputStream inputStream = file.getInputStream()){
                    ExcelLogs logs =new ExcelLogs();
                    Map<String, List<Param>> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);
                    List<Param> input = importExcel.get("Input");
                    if(!input.isEmpty()){

                        serviceInterface.setInput(input);
                        flag=true;
                    }
                    List<Param> output = importExcel.get("Output");
                    if(!output.isEmpty()){
                        // todo 在这里进一步解析数据,将数据解析成二进制或者其他类型 未解析 待启动服务的时候去解析
                        serviceInterface.setOutput(output);
                        System.out.println("serviceInterface 获取成功");
                        flag=true;
                    }
                    serviceInterface.setInterfaceId(UUID.randomUUID().toString().replace("-",""));
                    String originalFilename = file.getOriginalFilename();
                    int length = originalFilename.length();
                    String fileName = originalFilename.substring(0, length - 5);
                    serviceInterface.setInterfaceName(fileName);
                    InterfaceCache.put(serviceInterface.getInterfaceId(), serviceInterface);
                } catch (IOException e) {
                    log.error("读取Excel文件出错：{}", e.getMessage());
                }
            }
        }


        return flag;
    }

    @Override
    public boolean analysisFile(MultipartHttpServletRequest mreq){
        List<Map> maps = null;
        try {
            maps = ExcelUtils.analysisFile (mreq);
        } catch (Exception e) {
            return false;
        }
        if(maps==null){
            return false;
        }else{
            // 处理信息
            return true;
        }
    }
}
