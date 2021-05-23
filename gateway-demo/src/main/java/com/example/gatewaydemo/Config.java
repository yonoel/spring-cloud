package com.example.gatewaydemo;


import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.enums.ConfigSourceType;
import com.google.common.base.Function;

/**
 * 实现了配置接口，就只有2个方法实现了，string类型的property.
 *
 * @author yonoel 2021/05/19
 */
public class Config implements com.ctrip.framework.apollo.Config {
    @Override
    public String getProperty(String s, String s1) {
        if (s.equals("cxytiandi")) {
            return "demo";
        }
        return null;
    }

    @Override
    public Integer getIntProperty(String s, Integer integer) {
        return null;
    }

    @Override
    public Long getLongProperty(String s, Long aLong) {
        return null;
    }

    @Override
    public Short getShortProperty(String s, Short aShort) {
        return null;
    }

    @Override
    public Float getFloatProperty(String s, Float aFloat) {
        return null;
    }

    @Override
    public Double getDoubleProperty(String s, Double aDouble) {
        return null;
    }

    @Override
    public Byte getByteProperty(String s, Byte aByte) {
        return null;
    }

    @Override
    public Boolean getBooleanProperty(String s, Boolean aBoolean) {
        return null;
    }

    @Override
    public String[] getArrayProperty(String s, String s1, String[] strings) {
        return new String[0];
    }

    @Override
    public Date getDateProperty(String s, Date date) {
        return null;
    }

    @Override
    public Date getDateProperty(String s, String s1, Date date) {
        return null;
    }

    @Override
    public Date getDateProperty(String s, String s1, Locale locale, Date date) {
        return null;
    }

    @Override
    public <T extends Enum<T>> T getEnumProperty(String s, Class<T> aClass, T t) {
        return null;
    }

    @Override
    public long getDurationProperty(String s, long l) {
        return 0;
    }

    @Override
    public void addChangeListener(ConfigChangeListener configChangeListener) {

    }

    @Override
    public void addChangeListener(ConfigChangeListener configChangeListener, Set<String> set) {

    }


    @Override
    public boolean removeChangeListener(ConfigChangeListener configChangeListener) {
        return false;
    }

    @Override
    public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<>(1);
        names.add("cxytiandi");
        return names;

    }

    @Override
    public <T> T getProperty(String s, Function<String, T> function, T t) {
        return null;
    }

    @Override
    public ConfigSourceType getSourceType() {
        return null;
    }
}
