package com.loukou.jconsul.spring.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.loukou.jconsul.spring.ServiceRegisterLifecycle;

public class RegisterationParser extends AbstractSingleBeanDefinitionParser {
    private static final String LIFECYCLE_REGISTRATION_BEAN_NAME = "com.loukou.jconsul.spring.ServiceRegisterLifecycle";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServiceRegisterLifecycle.class;
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
            throws BeanDefinitionStoreException {
        return LIFECYCLE_REGISTRATION_BEAN_NAME;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {


        String name = element.getAttribute("name");
        if (StringUtils.hasLength(name)) {
            builder.addPropertyValue("serviceName", name);
        }

        String tag = element.getAttribute("tag");
        if (StringUtils.hasLength(tag)) {
            String[] tags = StringUtils.commaDelimitedListToStringArray(tag);
            builder.addPropertyValue("serviceTags", tags);
        }

        String port = element.getAttribute("port");
        if (StringUtils.hasLength(port)) {
            builder.addPropertyValue("servicePort", port);
        }

        String ttl = element.getAttribute("ttl");
        if (StringUtils.hasLength(ttl)) {
            builder.addPropertyValue("serviceCheckTtl", ttl);
        }

        String rate = element.getAttribute("rate");
        if (StringUtils.hasLength(rate)) {
            builder.addPropertyValue("serviceCheckPassPeriod", rate);
        }

        JConsulConfigUtils.registerJConsulAsRequired(parserContext.getRegistry(), null);

        builder.addPropertyReference("jconsul", JConsulConfigUtils.JCONSUL_BEAN_NAME);

        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

    }
}
