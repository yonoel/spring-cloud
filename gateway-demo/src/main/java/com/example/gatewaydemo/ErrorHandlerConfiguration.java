package com.example.gatewaydemo;


import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

/**
 * .
 *
 * @author yonoel 2021/05/17
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class ErrorHandlerConfiguration {
    private final ServerProperties serverProperties;
    private final ApplicationContext applicationContext;
    private final ResourceProperties resourceProperties;
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public ErrorHandlerConfiguration(ServerProperties serverProperties, ApplicationContext applicationContext, ResourceProperties resourceProperties, ObjectProvider<List<ViewResolver>> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolvers.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes){
        final JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler(
                errorAttributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext
        );
        jsonExceptionHandler.setViewResolvers(this.viewResolvers);
        jsonExceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        jsonExceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        return jsonExceptionHandler;
    }
}
