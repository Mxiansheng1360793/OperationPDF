package com.ze.pdf.operationpdf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author : ze
 * @Date : 2023/3/21 23:23
 * @Description : 获取resource配置类工具
 */
public class ConfigReaderUtil {
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static Properties properties;

    /**
     * 获取配置类
     * @return
     */
    public static Properties getProperties(){
        properties = new Properties();

        InputStream inputStream = ConfigReaderUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
