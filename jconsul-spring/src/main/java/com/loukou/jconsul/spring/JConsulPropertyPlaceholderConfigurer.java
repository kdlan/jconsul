package com.loukou.jconsul.spring;

import java.net.ConnectException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.loukou.jconsul.client.JConsul;

public class JConsulPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

    private Logger LOG = LoggerFactory.getLogger(JConsulPropertyPlaceholderConfigurer.class);

    private JConsul jconsul;

    private boolean fallbackToFileWhenNotExists = false;

    private boolean fallbackToFile = false;

    public JConsul getJconsul() {
        return jconsul;
    }

    public void setJconsul(JConsul jconsul) {
        this.jconsul = jconsul;
    }

    public boolean isFallbackToFileWhenNotExists() {
        return fallbackToFileWhenNotExists;
    }

    public void setFallbackToFileWhenNotExists(boolean fallbackToFileWhenNotExists) {
        this.fallbackToFileWhenNotExists = fallbackToFileWhenNotExists;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jconsul == null) {
            jconsul = new JConsul();
        }
        try {
            jconsul.agent().self();
            if (!fallbackToFileWhenNotExists) {
                setLocations((Resource[]) null);
            }
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof ConnectException) {
                LOG.debug(e.getMessage(), e);
                LOG.warn("No consul found, fallback to file properties");
                fallbackToFile = true;
            } else {
                throw e;
            }

        }
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        LOG.debug("query key {} from consul", placeholder);

        if (!fallbackToFile) {
            Optional<String> value = jconsul.keyValue().get(placeholder).raw().value();

            if (value.isPresent()) {
                return value.get();
            }
        }
        return super.resolvePlaceholder(placeholder, props);
    }
}
