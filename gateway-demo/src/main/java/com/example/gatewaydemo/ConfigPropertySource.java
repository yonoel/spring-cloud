package com.example.gatewaydemo;


import java.util.Set;

import com.ctrip.framework.apollo.spring.annotation.SpringValueProcessor;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * .
 *
 * @author yonoel 2021/05/19
 */
public class ConfigPropertySource extends EnumerablePropertySource<Config> {
    private static final String[] empty_array = new String[0];

    public ConfigPropertySource(String name, Config source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        SpringValueProcessor
        final Set<String> propertyNames = this.source.getPropertyNames();
        if (propertyNames.isEmpty()) {
            return empty_array;
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
    }

    @Override
    public Object getProperty(String s) {
        return this.source.getProperty(name, null);
    }
}
