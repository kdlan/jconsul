package com.loukou.jconsul.spring;

import org.springframework.beans.factory.annotation.Value;

public class PlaceholderTestBean {

    @Value("${__unittest/com.loukou.jconsul.spring.PlaceholderTestBean/value}")
    private String value;

    private String desc;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
