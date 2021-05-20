package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    @PostMapping("/exportExcel1")
    public void exportExcel(@RequestBody String json, HttpServletResponse response) throws IOException {
        JSONObject object = JSONObject.parseObject(json);
        excelUtilService.downLoadExcel(response, object);
    }


    /**
     * 导入excel
     *
     * @throws IOException
     */
    @PostMapping("/importExcel")
    public String importXls(MultipartHttpServletRequest request) throws IOException {
        return excelUtilService.upLoadExcel(request);
    }

    /**
     * 学生excel模板下载，可用于批量新建，修改学生对象，
     *
     * @param response
     * @param request
     * @return
     */
    @PostMapping(value = "/exportExcel", produces = "text/html;charset=UTF-8")
    public void downLoadStuInfoExcel(HttpServletResponse response, HttpServletRequest request) {
        try {
            //获取要下载的模板名称
            String fileName = "static/计划信息.xlsx";
            //设置要下载的文件的名称
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //通知客服文件的MIME类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //获取文件的路径
            ClassPathResource classPathResource = new ClassPathResource("static/计划信息.xlsx");
            String filePath  = classPathResource.getPath();
            InputStream input = classPathResource.getInputStream();
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
            input.close();
        } catch (Exception ex) {
            log.error("getApplicationTemplate :", ex);
        }
    }
}

