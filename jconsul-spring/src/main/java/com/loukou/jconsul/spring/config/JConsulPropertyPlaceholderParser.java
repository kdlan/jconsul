package com.loukou.jconsul.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.loukou.jconsul.spring.JConsulPropertyPlaceholderConfigurer;

public class JConsulPropertyPlaceholderParser extends AbstractSingleBeanDefinitionParser {

    private static final String SYSTEM_PROPERTIES_MODE_ATTRIB = "system-properties-mode";
    private static final String SYSTEM_PROPERTIES_MODE_DEFAULT = "ENVIRONMENT";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return JConsulPropertyPlaceholderConfigurer.class;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String location = element.getAttribute("location");
        if (StringUtils.hasLength(location)) {
            String[] locations = StringUtils.commaDelimitedListToStringArray(location);
            builder.addPropertyValue("locations", locations);
        }

        String propertiesRef = element.getAttribute("properties-ref");
        if (StringUtils.hasLength(propertiesRef)) {
            builder.addPropertyReference("properties", propertiesRef);
        }

        String fileEncoding = element.getAttribute("file-encoding");
        if (StringUtils.hasLength(fileEncoding)) {
            builder.addPropertyValue("fileEncoding", fileEncoding);
        }

        String order = element.getAttribute("order");
        if (StringUtils.hasLength(order)) {
            builder.addPropertyValue("order", Integer.valueOf(order));
        }

        builder.addPropertyValue("ignoreResourceNotFound",
                Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));

        builder.addPropertyValue("localOverride", Boolean.valueOf(element.getAttribute("local-override")));

        builder.addPropertyValue("ignoreUnresolvablePlaceholders",
                Boolean.valueOf(element.getAttribute("ignore-unresolvable")));

        String systemPropertiesModeName = element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIB);
        if (StringUtils.hasLength(systemPropertiesModeName)
                && !systemPropertiesModeName.equals(SYSTEM_PROPERTIES_MODE_DEFAULT)) {
            builder.addPropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_" + systemPropertiesModeName);
        }

        JConsulConfigUtils.registerJConsulAsRequired(parserContext.getRegistry(), null);
        builder.addPropertyReference("jconsul", JConsulConfigUtils.JCONSUL_BEAN_NAME);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    }

}
