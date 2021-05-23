package com.example.gatewaydemo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class GatewayDemoApplication {


    public static void main(String[] args) {
        System.setProperty("env","DEV");
//        final Config appConfig = ConfigService.getAppConfig();
        SpringApplication.run(GatewayDemoApplication.class, args);
    }

}
