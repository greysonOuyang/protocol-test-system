package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @PostMapping("/importExcel")
    public String importXls(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        boolean flag = false;
        if(request instanceof  MultipartHttpServletRequest){
            multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            flag = excelUtilService.upLoadExcel(multipartHttpServletRequest);
        }
        if(!flag){
            return "失败了";
        }
        return "成功了";
    }
}

