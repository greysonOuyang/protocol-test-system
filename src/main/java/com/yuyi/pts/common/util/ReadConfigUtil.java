package com.yuyi.pts.common.util;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @description:
 * @author: wzl
 * @date:2021-05-18 13:52
 */
public class ReadConfigUtil {

   private static Properties properties = new Properties();
    @PostConstruct
    public static String getValue(String key) throws IOException {
        InputStream inputStream = null;
        String result =null ;
        try {
            inputStream = ReadConfigUtil.class.getResourceAsStream("context_config.properties");
            properties.load(new InputStreamReader(inputStream, "UTF-8"));
            result = properties.getProperty(key);
        } catch (Exception e) {
        } finally {
            inputStream.close();
        }
        return  result;
    }
}
