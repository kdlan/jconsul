package com.loukou.jconsul.spring;

import org.springframework.beans.factory.annotation.Value;

public class PlaceholderTestBean {

    @Value("${__unittest/com.loukou.jconsul.spring.PlaceholderTestBean/value}")
    private String value;

    private String desc;

    private String kv;

    @Value("#{jconsulConfig.serviceAddress('config_unittest')}")
    private String address;

    private String randomAddress;

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

    public String getKv() {
        return kv;
    }

    public void setKv(String kv) {
        this.kv = kv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRandomAddress() {
        return randomAddress;
    }

    public void setRandomAddress(String randomAddress) {
        this.randomAddress = randomAddress;
    }



}
