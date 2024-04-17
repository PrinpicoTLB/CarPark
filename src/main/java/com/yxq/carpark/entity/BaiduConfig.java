package com.yxq.carpark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaiduConfig {
    private String accessToken;
    private String expireIn;
}
