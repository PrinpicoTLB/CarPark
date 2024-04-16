//package com.yxq.carpark.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//
//@Configuration
//public class REdisConfig {
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(){
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setPassword("123456");
//        configuration.setHostName("localhost");
//        configuration.setPassword("123456");
//        return new JedisConnectionFactory(configuration);
//    }
//}
