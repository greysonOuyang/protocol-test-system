package com.yuyi.pts.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.service.ExcelUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

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
      // boolean flag = excelUtilService.downLoadExcel(response,object);
     //  if(flag){
           try {
               Thread.sleep(1000);
               Resource resource = new ClassPathResource("/test.xls");
               File file = resource.getFile();
               String filename = resource.getFilename();
               InputStream inputStream = new FileInputStream(file);
               //强制下载不打开
               response.setContentType("application/force-download");
               OutputStream out = response.getOutputStream();
               //使用URLEncoder来防止文件名乱码或者读取错误
               response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(filename, "UTF-8"));
               int b = 0;
               byte[] buffer = new byte[1000000];
               while (b != -1) {
                   b = inputStream.read(buffer);
                   if(b!=-1) {
                       out.write(buffer, 0, b);
                   }
               }
               inputStream.close();
               out.close();
               out.flush();
           } catch (IOException | InterruptedException e) {
               e.printStackTrace();
           }
       }
   // }


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

