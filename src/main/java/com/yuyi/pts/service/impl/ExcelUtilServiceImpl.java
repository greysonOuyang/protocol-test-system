package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.cache.PlanInfoCache;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.MapAndListConstant;
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
            //   给PlanInfo赋初始值 表格内容赋值
            PlanInfo planInfo = MapAndListConstant.PLANINFO;
            Collection<Object> dataset = new ArrayList<Object>();
            for (int i = 0; i < planInfo.getModelList().size(); i++) {
                dataset.add(planInfo.getModelList().get(i));
            }
            String date = DateTimeUtil.getStringDateShort();
            String fileName = interfaceName.toUpperCase()+"_" + date +".xls";
            //  给表头加数据
            Map map = MapAndListConstant.getPlanModel();
            ExcelUtil.exportExcel(map, dataset, response,fileName);
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
                    if(!importExcel.isEmpty()){
                        // todo 在这里进一步解析数据,将数据解析成二进制或者其他类型 未解析 待启动服务的时候去解析
                        serviceInterface.setInput(importExcel.get("sheel1"));
                        serviceInterface.setOutput(importExcel.get("sheel2"));
                        System.out.println("serviceInterface 获取成功");
                        flag=true;
                    }
                    serviceInterface.setInterfaceId(UUID.randomUUID().toString().replace("-",""));
                    serviceInterface.setInterfaceName(file.getOriginalFilename());
                    PlanInfoCache.put(file.getOriginalFilename(),serviceInterface);
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
