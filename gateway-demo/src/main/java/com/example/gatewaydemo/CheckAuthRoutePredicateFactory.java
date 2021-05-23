package com.example.gatewaydemo;


import java.util.function.Predicate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * .
 *
 * @author yonoel 2021/05/17
 */
@Component
@Slf4j
public class CheckAuthRoutePredicateFactory extends
        AbstractRoutePredicateFactory<CheckAuthRoutePredicateFactory.Config> {
    public CheckAuthRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return serverWebExchange -> {
            log.info("进入断言");
            if (config.name.equals("demo")) {
                return true;
            }
            return false;
        };
    }

    @Getter
    @Setter
    public static class Config {
        private String name;
    }
}
