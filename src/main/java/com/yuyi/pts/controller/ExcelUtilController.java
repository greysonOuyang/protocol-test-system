package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ResultEntity;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
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
     *
     * @throws IOException
     */
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody String json, HttpServletResponse response) throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        excelUtilService.downLoadExcel(response, object);
    }


    /**
     * 上传excel
     *
     * @throws IOException
     */
    @PostMapping("/importExcel")
    public String importXls(MultipartHttpServletRequest request) throws IOException {
        boolean flag = false;
        flag = excelUtilService.upLoadExcel(request);
        if(!flag){
            return ResultEntity.successWithData("文件上传成功");
        }
        return "成功了";
    }
}

