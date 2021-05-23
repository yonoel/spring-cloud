package com.example.gatewaydemo;


/**
 * .
 *
 * @author yonoel 2021/05/19
 */


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware {
    String APOLLO_PROPERTY_SOURCE_NAME = "APOLLO_PROPERTY_SOURCE_NAME";
    private ConfigurableEnvironment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        final ConfigPropertySource application = new ConfigPropertySource("application", new Config());
        final CompositePropertySource compositePropertySource = new CompositePropertySource(APOLLO_PROPERTY_SOURCE_NAME);
        compositePropertySource.addPropertySource(application);
        this.environment.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

}

