package com.yxq.carpark.service;

/**
 * 百度API服务接口
 */
public interface BaiduService {

    /**
     * 获取百度API的AccessToken
     * @return
     */
    String getAccessToken();

    String licensePlate(String filePath);
}
