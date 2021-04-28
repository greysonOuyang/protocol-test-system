package com.yuyi.pts.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ASUS
 */
@Service
public interface ExcelUtilService {
    /**
     * 下载excel
     */
     boolean downLoadExcel(HttpServletResponse response, JSONObject object) throws IOException;

    /**
     * 上传excel
     */
    void upLoadExcel(MultipartHttpServletRequest mreq) throws IOException;
     boolean analysisFile(MultipartHttpServletRequest mreq);
}
