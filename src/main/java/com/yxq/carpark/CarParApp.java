package com.yxq.carpark;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.yxq.carpark.dao")
@Slf4j
public class CarParApp {
    public static void main(String[] args) {
        SpringApplication.run(CarParApp.class,args);
        log.info("******** 停车管理系统启动成功 **********");
    }

}
