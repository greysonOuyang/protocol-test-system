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
     void downLoadExcel(HttpServletResponse response, JSONObject object) throws IOException;

    /**
     * 导入excel
     *
     * @return
     */
    String upLoadExcel(MultipartHttpServletRequest mreq) throws IOException;

    /**
     * 备用  暂时为用到
     * @param mreq
     * @return
     */
    boolean analysisFile(MultipartHttpServletRequest mreq);
}
