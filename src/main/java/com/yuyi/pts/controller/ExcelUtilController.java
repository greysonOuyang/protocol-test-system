package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.constant.ExcelConstant;
import com.yuyi.pts.common.constant.MapAndListConstant;
import com.yuyi.pts.model.excel.ExcelLogs;
import com.yuyi.pts.model.excel.ExcelUtil;
import com.yuyi.pts.model.vo.response.PlanInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * @author : wzl
 * @date : 2021/4/27/11:11
 * @description:
 */
@RestController("/api/excelUtil")
@Slf4j
public class ExcelUtilController {
    /**
     *
     * 下载excel
     * @throws IOException
     */
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody String json)  throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        getExportExcel(object);
    }

    public static void getExportExcel(JSONObject object) throws IOException {
        // 获取到接口名称，根据接口名称去配置对应的excel文件
        String interfaceName = object.get("interfaceName").toString();
        if(ExcelConstant.PLAN_INFO.equals(interfaceName)){
            //   给PlanInfo赋初始值 表格内容赋值
            PlanInfo planInfo = MapAndListConstant.PLANINFO;
            Collection<Object> dataset=new ArrayList<Object>();
            for (int i = 0; i < planInfo.getModelList().size(); i++) {
                dataset.add(planInfo.getModelList().get(i));
            }
            File f= new File("test.xls");
            OutputStream out = new FileOutputStream(f);
            //  给表头加数据
            Map map = MapAndListConstant.getPlanModel();
            ExcelUtil.exportExcel(map, dataset,out);
            out.close();
        }

    }

    /**
     *
     * 上传excel
     * @throws IOException
     */
    @GetMapping("/importExcel")
    public void importXls() throws FileNotFoundException {
        getImportExcel();
    }

    public static void getImportExcel() throws FileNotFoundException {
        File f=new File("src/test/resources/test.xls");
        getMap(f);
    }

    public static void getMap(File f) throws FileNotFoundException {
        InputStream inputStream= new FileInputStream(f);

        ExcelLogs logs =new ExcelLogs();
        Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);

        for(Map m : importExcel){
            System.out.println(m);
        }
    }
}

