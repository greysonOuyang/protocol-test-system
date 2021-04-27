package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * @author : wzl
 * @date : 2021/4/27/11:11
 * @description:
 */
@RestController("/api/excelUtil")
@Slf4j
public class ExcelUtilController {

    @Autowired
    private ExcelUtilService excelUtilService;
    /**
     *
     * 下载excel
     * @throws IOException
     */
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody String json)  throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        excelUtilService.downLoadExcel(object);
    }


    /**
     *
     * 上传excel
     * @throws IOException
     */
    @GetMapping("/importExcel")
    public void importXls(@RequestBody String json) throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        excelUtilService.upLoadExcel(object);
    }
}

