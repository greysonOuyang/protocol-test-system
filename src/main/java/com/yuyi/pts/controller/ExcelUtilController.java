package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.constant.ExcelConstant;
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
@RestController
@RequestMapping("/api/excelUtil")
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
            // 填充planInfo对象数据
            PlanInfo planInfo = new PlanInfo();
            PlanInfo planInfo1 = new PlanInfo();
            // todo  给PlanInfo赋初始值
            File f= new File("test.xls");
            List<Map<String,PlanInfo>> list = new ArrayList<>();
            Map<String,PlanInfo> map = new HashMap<>();
            map.put("planInfo",planInfo);
            map.put("planInfo1",planInfo1);
            list.add(map);
            OutputStream out = new FileOutputStream(f);
            Map<String,String> headerMap = new LinkedHashMap<>();
            // todo  给map 赋值
            ExcelUtil.exportExcel(headerMap, list,out);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map =new LinkedHashMap<>();
        map.put("name", "");
        map.put("age", "");
        map.put("birthday","");
        map.put("sex","");
        Map<String,Object> map2 =new LinkedHashMap<String, Object>();
        map2.put("name", "测试是否是中文长度不能自动宽度.测试是否是中文长度不能自动宽度.");
        map2.put("age", null);
        map2.put("sex", null);
        map.put("birthday",null);
        Map<String,Object> map3 =new LinkedHashMap<String, Object>();
        map3.put("name", "张三");
        map3.put("age", 12);
        map3.put("sex", "男");
        map3.put("birthday",new Date());
        list.add(map);
        list.add(map2);
        list.add(map3);
        Map<String,String> map1 = new LinkedHashMap<>();
        map1.put("name","姓名");
        map1.put("age","年龄");
        map1.put("birthday","出生日期");
        map1.put("sex","性别");
        File f= new File("test.xls");
        OutputStream out = new FileOutputStream(f);
        ExcelUtil.exportExcel(map1,list, out );
        out.close();
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

