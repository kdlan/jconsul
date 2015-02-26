package com.loukou.jconsul.client;

import java.util.List;

import com.google.common.reflect.TypeToken;
import com.loukou.jconsul.client.model.HealthCheck;
import com.loukou.jconsul.client.model.HealthService;

public class HealthRequestBuilder extends JConsulRequestBuilder {

    @SuppressWarnings("serial")
    private static final TypeToken<List<HealthCheck>> LIST_CHECK_TOKEN = new TypeToken<List<HealthCheck>>() {
    };

    @SuppressWarnings("serial")
    private static final TypeToken<List<HealthService>> LIST_SERVICE_TOKEN = new TypeToken<List<HealthService>>() {
    };

    HealthRequestBuilder(RequestProcessor processor) {
        super(processor);
    }

    public NodeRequestBuilder node(String node) {
        return new NodeRequestBuilder(node);
    }

    public CheckRequestBuilder checks(String serviceName) {
        return new CheckRequestBuilder(serviceName);
    }

    public ServiceRequestBuilder service(String serviceName) {
        return new ServiceRequestBuilder(serviceName);
    }

    public StateRequestBuilder state(String state){
        return new StateRequestBuilder(state);
    }

    public class NodeRequestBuilder extends DirectResultBuilder<NodeRequestBuilder, List<HealthCheck>> {

        private NodeRequestBuilder(String node) {
            super("/health/node/" + node, LIST_CHECK_TOKEN, NodeRequestBuilder.class, HealthRequestBuilder.this);
        }

    }

    public class CheckRequestBuilder extends DirectResultBuilder<CheckRequestBuilder, List<HealthCheck>> {
        private CheckRequestBuilder(String serviceName) {
            super("/health/checks/" + serviceName, LIST_CHECK_TOKEN, CheckRequestBuilder.class, HealthRequestBuilder.this);
        }
    }

    public class ServiceRequestBuilder extends DirectResultBuilder<ServiceRequestBuilder, List<HealthService>> {
        private ServiceRequestBuilder(String serviceName) {
            super("/health/service/" + serviceName, LIST_SERVICE_TOKEN, ServiceRequestBuilder.class,
                    HealthRequestBuilder.this);
        }

        public ServiceRequestBuilder tag(String tag) {
            addParameter("tag", tag);
            return this;
        }

        public ServiceRequestBuilder passing() {
            addParameter("passing");
            return this;
        }
    }

    public class StateRequestBuilder extends DirectResultBuilder<StateRequestBuilder, List<HealthCheck>> {
        private StateRequestBuilder(String state) {
            super("/health/state/" + state, LIST_CHECK_TOKEN, StateRequestBuilder.class, HealthRequestBuilder.this);
        }
    }
}
