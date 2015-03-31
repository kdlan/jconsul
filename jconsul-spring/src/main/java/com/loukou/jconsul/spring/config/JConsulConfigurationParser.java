package com.loukou.jconsul.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class JConsulConfigurationParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return JConsulConfigUtils.registerJConsulConfigurationAsRequired(parserContext.getRegistry(), null);
    }
}
