package com.loukou.jconsul.spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.loukou.jconsul.client.AgentRequestBuilder.RegisterStatus;
import com.loukou.jconsul.client.AgentRequestBuilder.ServiceRegisterBuilder;
import com.loukou.jconsul.client.JConsul;

public class ServiceRegisterLifecycle implements SmartLifecycle, InitializingBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegisterLifecycle.class);

    private static final int DEFAULT_PORT = 8080;

    private static final int DEFAULT_TTL_SECONDS = 10;

    private static final int DEFAULT_PASS_PERIOD_SECONDS = 3;

    private JConsul jconsul;
    private String serviceName;
    private int servicePort;
    private String serviceId;
    private String[] serviceTags;
    private int serviceCheckTtl = DEFAULT_TTL_SECONDS;
    private int serviceCheckPassPeriod = DEFAULT_PASS_PERIOD_SECONDS;

    private ScheduledExecutorService scheduledExecutorService;

    private volatile RegisterStatus serviceRegisterStatus;
    private volatile ScheduledFuture<?> passTask;

    private volatile boolean running;

    public JConsul getJconsul() {
        return jconsul;
    }

    public void setJconsul(JConsul jconsul) {
        this.jconsul = jconsul;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String[] getServiceTags() {
        return serviceTags;
    }

    public void setServiceTags(String[] serviceTags) {
        this.serviceTags = serviceTags;
    }

    public int getServiceCheckTtl() {
        return serviceCheckTtl;
    }

    public void setServiceCheckTtl(int serviceCheckTtl) {
        this.serviceCheckTtl = serviceCheckTtl;
    }

    public int getServiceCheckPassPeriod() {
        return serviceCheckPassPeriod;
    }

    public void setServiceCheckPassPeriod(int serviceCheckPassPeriod) {
        this.serviceCheckPassPeriod = serviceCheckPassPeriod;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jconsul == null) {
            jconsul = new JConsul();
        }

        Preconditions.checkNotNull(serviceName, "service name can not be null");

        if (servicePort == 0) {
            servicePort = DEFAULT_PORT;
            LOG.warn("Port not set, use 8080 as default");
        }

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
                "jconsul-spring-lifecycle-thread-%d").build());

        serviceId = serviceName + ":" + servicePort;
        LOG.info("Register service {} with id {} to consul, check ttl {}", serviceName, serviceId, serviceCheckTtl);
        ServiceRegisterBuilder builder = jconsul.agent().registerService(serviceName).id(serviceId).port(servicePort);
        if (serviceTags != null) {
            builder.tags(serviceTags);
        }
        serviceRegisterStatus = builder.ttl(serviceCheckTtl);
    }

    @Override
    public void destroy() throws Exception {
        scheduledExecutorService.shutdownNow();
        if (serviceRegisterStatus != null) {
            serviceRegisterStatus.deregister();
            serviceRegisterStatus = null;
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        LOG.info("Service {} with id {} started", serviceName, serviceId);
        passTask = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    serviceRegisterStatus.pass();
                } catch (Exception e) {
                    LOG.warn("consul serivce pass error, msg: {}", e.getMessage());
                }
            }
        }, 0, serviceCheckPassPeriod, TimeUnit.SECONDS);
        running = true;
    }

    @Override
    public void stop() {
        running = false;
        if (passTask != null) {
            passTask.cancel(false);
            passTask = null;
        }
        try {
            serviceRegisterStatus.fail("service stopped");
        } catch (Exception e) {
            LOG.warn("consul serivce fail error, msg: {}", e.getMessage());
        }
        LOG.info("Service {} with id {} stopped", serviceName, serviceId);

    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

}
