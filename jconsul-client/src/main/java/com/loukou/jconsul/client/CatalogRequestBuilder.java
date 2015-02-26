package com.loukou.jconsul.client;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.loukou.jconsul.client.CatalogRegisterBuilder.NodeBuilder;
import com.loukou.jconsul.client.model.CatalogNode;
import com.loukou.jconsul.client.model.CatalogService;
import com.loukou.jconsul.client.model.Node;

public class CatalogRequestBuilder extends JConsulRequestBuilder {

    @SuppressWarnings("serial")
    private static final TypeToken<List<Node>> LIST_NODE_TOKEN = new TypeToken<List<Node>>() {
    };

    @SuppressWarnings("serial")
    private static final TypeToken<List<CatalogService>> LIST_SERVICE_TOKEN = new TypeToken<List<CatalogService>>() {
    };

    @SuppressWarnings("serial")
    private static final TypeToken<Map<String, List<String>>> MAP_STRING_LIST_STRING_TOKEN = new TypeToken<Map<String, List<String>>>() {
    };

    private static final TypeToken<CatalogNode> NODE_TOKEN = TypeToken.of(CatalogNode.class);

    CatalogRequestBuilder(RequestProcessor processor) {
        super(processor);
    }

    public NodeBuilder register(String node, String address) {
        return new CatalogRegisterBuilder(this).newBuilder(node, address);
    }

    public DeregisterRequestBuilder deregister(String node) {
        return new DeregisterRequestBuilder(node);
    }

    @SuppressWarnings("serial")
    public List<String> datacenters() {
        return getJsonResult("GET", "/catalog/datacenters", new TypeToken<List<String>>() {
        });
    }

    public NodeListRequestBuilder nodes() {
        return new NodeListRequestBuilder();
    }

    public ServiceListRequestBuilder serivces() {
        return new ServiceListRequestBuilder();
    }

    public ServiceRequestBuilder service(String serviceName) {
        return new ServiceRequestBuilder(serviceName);
    }

    public NodeRequestBuilder node(String node) {
        return new NodeRequestBuilder(node);
    }

    public class ServiceListRequestBuilder extends
            DirectResultBuilder<ServiceListRequestBuilder, Map<String, List<String>>> {

        private ServiceListRequestBuilder() {
            super("/catalog/services", MAP_STRING_LIST_STRING_TOKEN, ServiceListRequestBuilder.class,
                    CatalogRequestBuilder.this);
        }
    }

    public class ServiceRequestBuilder extends DirectResultBuilder<ServiceRequestBuilder, List<CatalogService>> {

        private ServiceRequestBuilder(String serviceName) {
            super("/catalog/service/" + serviceName, LIST_SERVICE_TOKEN, ServiceRequestBuilder.class,
                    CatalogRequestBuilder.this);

        }

        public ServiceRequestBuilder tag(String tag) {
            addParameter("tag", tag);
            return this;
        }
    }

    public class NodeListRequestBuilder extends DirectResultBuilder<NodeListRequestBuilder, List<Node>> {
        private NodeListRequestBuilder() {
            super("/catalog/nodes", LIST_NODE_TOKEN, NodeListRequestBuilder.class, CatalogRequestBuilder.this);
        }
    }

    public class NodeRequestBuilder extends DirectResultBuilder<NodeRequestBuilder, CatalogNode> {

        private NodeRequestBuilder(String node) {
            super("/catalog/node/" + node, NODE_TOKEN, NodeRequestBuilder.class, CatalogRequestBuilder.this);
        }
    }

    public class DeregisterRequestBuilder {

        private final Map<String, String> data = Maps.newHashMap();

        public DeregisterRequestBuilder(String node) {
            data.put("Node", node);
        }

        public DeregisterRequestBuilder datacenter(String dc) {
            data.put("Datacenter", dc);
            return this;
        }

        public DeregisterRequestBuilder service(String serviceID) {
            data.put("ServiceID", serviceID);
            return this;
        }

        public DeregisterRequestBuilder check(String checkID) {
            data.put("CheckID", checkID);
            return this;
        }

        public void execute() {
            setJsonBody(data);
            getPlainResult("PUT", "/catalog/deregister");
        }

    }

}
