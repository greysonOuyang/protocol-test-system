package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author : wzl
 * @date : 2021/4/27/11:11
 * @description:
 */
@RestController
@RequestMapping("/api/excelUtil")
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
    public void exportExcel(@RequestBody String json, HttpServletResponse response)  throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        excelUtilService.downLoadExcel(response,object);
    }


    /**
     *
     * 上传excel
     * @throws IOException
     */
    @GetMapping("/importExcel")
    public String importXls(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest mreq = null;
        boolean flag = false;
        if(request instanceof  MultipartHttpServletRequest){
            mreq = (MultipartHttpServletRequest) request;
            //flag = excelUtilService.analysisFile(mreq);
            flag = excelUtilService.upLoadExcel(mreq);
        }
        if(!flag){
            return "失败了";
        }
        return "成功了";
    }
}

