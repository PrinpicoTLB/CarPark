package com.yxq.carpark;

import com.yxq.carpark.service.BaiduService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class baiduTest {
    @Autowired
    private BaiduService baiduService;

    @Test
    void tokenTest() {
        String accessToken = baiduService.getAccessToken();
        System.out.println("token: " + accessToken);
    }

    @Test
    void licensePlateTest() {
//        String s = baiduService.licensePlate("D:\\javaResource\\05\\code\\CarPark\\src\\resource\\img\\test1.jpg");
//        System.out.println(s);
    }
}
