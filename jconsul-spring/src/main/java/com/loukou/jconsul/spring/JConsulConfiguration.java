package com.loukou.jconsul.spring;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.loukou.jconsul.client.HealthRequestBuilder.ServiceRequestBuilder;
import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.model.HealthService;
import com.loukou.jconsul.client.model.Service;

public class JConsulConfiguration implements InitializingBean {

    private JConsul jconsul;
    private Random random;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jconsul == null) {
            jconsul = new JConsul();
        }
        random = new Random();
    }

    public void setJconsul(JConsul jconsul) {
        this.jconsul = jconsul;
    }

    public String config(String key) {
        return config(key, null);
    }

    public String config(String key, String defaultValue) {
        Optional<String> value = jconsul.keyValue().get(key).raw().value();
        return value.isPresent() ? value.get() : defaultValue;
    }

    public String randomAddress(String name, boolean passing) {
        ServiceRequestBuilder builder = jconsul.health().service(name);
        if (passing) {
            builder = builder.passing();
        }

        Optional<List<HealthService>> result = builder.value();

        if (result.isPresent()) {
            List<HealthService> list = result.get();
            if (list.isEmpty()) {
                return null;
            }
            HealthService healthService = list.get(random.nextInt(list.size()));
            return getAddress(healthService);
        } else {
            return null;
        }
    }

    public String serviceAddrs(String name) {
        return serviceAddrs(name, ",", true);
    }

    public String serviceAllAddress(String name) {
        return serviceAddrs(name, ",", false);
    }

    public String serviceAddrs(String name, String separator, boolean passing) {
        ServiceRequestBuilder builder = jconsul.health().service(name);
        if (passing) {
            builder = builder.passing();
        }

        Optional<List<HealthService>> result = builder.value();

        if (result.isPresent()) {
            List<HealthService> list = result.get();
            return Joiner.on(separator).join(Lists.transform(list, new Function<HealthService, String>() {
                @Override
                public String apply(HealthService input) {
                    return getAddress(input);
                }
            }));
        } else {
            return null;
        }
    }

    private String getAddress(HealthService input) {
        Service service = input.getService();
        String addr = service.getAddress();
        if (!StringUtils.hasLength(addr)) {
            addr = input.getNode().getAddress();
        }
        return addr + ":" + service.getPort();

    }

}
