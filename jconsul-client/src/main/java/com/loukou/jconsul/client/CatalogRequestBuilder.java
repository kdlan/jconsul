package com.loukou.jconsul.client;

import java.util.List;
import java.util.Map;

import com.loukou.jconsul.client.CatalogRegisterBuilder.NodeBuilder;
import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.catalog.CatalogNode;
import com.loukou.jconsul.client.model.catalog.CatalogService;
import com.loukou.jconsul.client.model.health.Node;
import com.loukou.jconsul.client.util.JConsulUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;

public class CatalogRequestBuilder extends JConsulRequestBuilder {

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

    public ServiceRequestBuilder service(String service) {
        return new ServiceRequestBuilder(service);
    }

    public NodeRequestBuilder node(String node) {
        return new NodeRequestBuilder(node);
    }

    private abstract class ResultBuilder<T extends ResultBuilder<T, V>, V> extends
            BlockingAndConsistencyBuilder<ResultBuilder<T, V>> {
        private final String path;
        private final TypeToken<V> type;

        private ResultBuilder(Class<T> subClass, String path, TypeToken<V> type) {
            super(subClass, CatalogRequestBuilder.this);
            this.path = path;
            this.type = type;
        }

        public Optional<V> value() {
            return response().getResult();
        }

        public JConsulResponse<V> response() {
            return getJConsulResponse("GET", path, type);
        }

        public void async(JConsulResponseCallback<V> callback) {
            executeResult("GET", path, type, callback);
        }
    }

    public class ServiceListRequestBuilder extends ResultBuilder<ServiceListRequestBuilder, Map<String, List<String>>> {
        @SuppressWarnings("serial")
        private ServiceListRequestBuilder() {
            super(ServiceListRequestBuilder.class, "/catalog/services", new TypeToken<Map<String, List<String>>>() {
            });
        }
    }

    public class ServiceRequestBuilder extends BlockingAndConsistencyBuilder<ServiceRequestBuilder> {
        @SuppressWarnings("serial")
        private final TypeToken<List<CatalogService>> type = new TypeToken<List<CatalogService>>() {
        };
        private final String service;

        private ServiceRequestBuilder(String service) {
            super(ServiceRequestBuilder.class, CatalogRequestBuilder.this);
            this.service = service;

        }

        public ServiceRequestBuilder tag(String tag){
            addParameter("tag",tag);
            return this;
        }

        public Optional<CatalogService> value() {
            return response().getResult();
        }

        public JConsulResponse<CatalogService> response() {
            JConsulResponse<List<CatalogService>> response = getJConsulResponse("GET", "/catalog/service/" + service,
                    type);

            return JConsulUtils.transform(response);

        }

        public void async(JConsulResponseCallback<CatalogService> callback) {
            executeResult("GET", "/catalog/service/" + service, type, JConsulUtils.transform(callback));
        }
    }

    public class NodeListRequestBuilder extends ResultBuilder<NodeListRequestBuilder, List<Node>> {
        @SuppressWarnings("serial")
        private NodeListRequestBuilder() {
            super(NodeListRequestBuilder.class, "/catalog/nodes", new TypeToken<List<Node>>() {
            });
        }
    }

    public class NodeRequestBuilder extends ResultBuilder<NodeRequestBuilder, CatalogNode> {

        private NodeRequestBuilder(String node) {
            super(NodeRequestBuilder.class, "/catalog/node/" + node, TypeToken.of(CatalogNode.class));
        }
    }

    public class DeregisterRequestBuilder{

        private final Map<String,String> data=Maps.newHashMap();


        public DeregisterRequestBuilder(String node) {
            data.put("Node", node);
        }
        public DeregisterRequestBuilder datacenter(String dc){
            data.put("Datacenter", dc);
            return this;
        }

        public DeregisterRequestBuilder service(String serviceID){
            data.put("ServiceID", serviceID);
            return this;
        }

        public DeregisterRequestBuilder check(String checkID){
            data.put("CheckID", checkID);
            return this;
        }


        public void execute(){
            setJsonBody(data);
            getPlainResult("PUT", "/catalog/deregister");
        }

    }

}
