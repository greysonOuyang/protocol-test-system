package com.yuyi.pts.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ASUS
 */
@Service
public interface ExcelUtilService {
    /**
     * 下载excel
     */
     void downLoadExcel(JSONObject object) throws IOException;

    /**
     * 上传excel
     */
    void upLoadExcel(JSONObject object) throws IOException;
}
