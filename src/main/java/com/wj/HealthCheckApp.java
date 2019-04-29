package com.wj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author jun.wang
 * @title: HealthCheckApp
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/4/28 13:51
 */

@SpringBootApplication
@EnableEurekaClient
public class HealthCheckApp {

    public static void main(String args[]) {
        SpringApplication.run(HealthCheckApp.class, args);
    }

    @LoadBalanced //开启负载均衡客户端
    @Bean//注册一个具有容错功能的RestTemplate
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
