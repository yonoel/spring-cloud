package com.example.zuulfiledemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ZuulFileDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulFileDemoApplication.class, args);
    }

}
