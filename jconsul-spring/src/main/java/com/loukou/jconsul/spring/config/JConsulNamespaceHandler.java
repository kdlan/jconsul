package com.loukou.jconsul.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class JConsulNamespaceHandler extends NamespaceHandlerSupport{

    @Override
    public void init() {
        registerBeanDefinitionParser("configuration", new JConsulConfigurationParser());
        registerBeanDefinitionParser("property-placeholder", new JConsulPropertyPlaceholderParser());
        registerBeanDefinitionParser("registration", new RegisterationParser());
    }
}
