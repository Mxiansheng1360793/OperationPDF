package com.ze.pdf.utils;

import java.net.URL;

/**
 * @Author : ze
 * @Date : 2023/3/21 23:08
 * @Description : 获取资源路径工具类
 */
public class ResourceUtils {

    public static String getResourcePath(){
        URL url = ResourceUtils.class.getClassLoader().getResource("");
        return url.getPath();
    }
}
