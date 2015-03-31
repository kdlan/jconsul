package com.loukou.jconsul.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;

import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.spring.JConsulConfiguration;

public class JConsulConfigUtils {
    public static final String JCONSUL_BEAN_NAME = "com.loukou.jconsul.client.JConsul";

    public static final String JCONSUL_CONFIGURATION_BEAN_NAME = "com.loukou.jconsul.spring.JConsulConfiguration";
    public static final String JCONSUL_CONFIGURATION_ALIAS = "jconsulConfig";

    public static BeanDefinition registerJConsulAsRequired(BeanDefinitionRegistry registry, Object source) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry.containsBeanDefinition(JCONSUL_BEAN_NAME)) {
            return null;
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition(JConsul.class);
        beanDefinition.setSource(source);
        beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDefinition.setDestroyMethodName("close");
        registry.registerBeanDefinition(JCONSUL_BEAN_NAME, beanDefinition);
        return beanDefinition;
    }

    public static BeanDefinition registerJConsulConfigurationAsRequired(BeanDefinitionRegistry registry, Object source) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry.containsBeanDefinition(JCONSUL_CONFIGURATION_BEAN_NAME)) {
            return null;
        }

        registerJConsulAsRequired(registry, source);

        RootBeanDefinition beanDefinition = new RootBeanDefinition(JConsulConfiguration.class);
        beanDefinition.setSource(source);
        beanDefinition.getPropertyValues().add("jconsul", new RuntimeBeanReference(JCONSUL_BEAN_NAME));
        beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
        registry.registerBeanDefinition(JCONSUL_CONFIGURATION_BEAN_NAME, beanDefinition);
        registry.registerAlias(JCONSUL_CONFIGURATION_BEAN_NAME, JCONSUL_CONFIGURATION_ALIAS);
        return beanDefinition;
    }
}