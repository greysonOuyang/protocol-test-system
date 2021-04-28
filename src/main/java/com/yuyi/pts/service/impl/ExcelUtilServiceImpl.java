package com.yuyi.pts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.MapAndListConstant;
import com.yuyi.pts.common.util.DateTimeUtil;
import com.yuyi.pts.common.util.ExcelUtils;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.vo.response.PlanInfo;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    public boolean upLoadExcel(MultipartHttpServletRequest mreq) throws IOException {
        // todo 将文件解析，存入缓存，在启动服务端程序在拿出来使用
        boolean flag = false;
        InputStream inputStream= null;
        inputStream = mreq.getFile ("file").getInputStream ();
        ExcelLogs logs =new ExcelLogs();
        Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);
         if(!importExcel.isEmpty()){
                    flag=true;
           }
        for(Map m : importExcel){
            // todo 在这里进一步解析数据,将数据解析成二进制或者其他类型

            System.out.println("输出结果为："+m);
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
