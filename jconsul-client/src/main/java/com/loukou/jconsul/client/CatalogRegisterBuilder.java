package com.loukou.jconsul.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CatalogRegisterBuilder {
    private final Map<String, Object> map = new HashMap<>();
    private final CatalogRequestBuilder builder;

    CatalogRegisterBuilder(CatalogRequestBuilder builder) {
        this.builder = builder;
    }

    public void execute() {
        builder.setJsonBody(map);
        builder.getPlainResult("PUT", "/catalog/register");
    }

    NodeBuilder newBuilder(String node, String address) {
        return new NodeBuilder(node, address);
    }

    public class NodeBuilder {

        private NodeBuilder(String node, String address) {
            map.put("Node", node);
            map.put("Address", address);
        }

        public NodeBuilder datacenter(String datacenter) {
            map.put("Datacenter", datacenter);
            return this;
        }

        public ServiceBuilder service(String id, String name) {
            return new ServiceBuilder(id, name);
        }

        public CheckBuilder check(String id, String name) {
            return new CheckBuilder(id, name);
        }

        public void execute() {
            CatalogRegisterBuilder.this.execute();
        }

    }

    public class ServiceBuilder {
        private final Map<String, Object> service = new HashMap<>();

        private ServiceBuilder(String id, String name) {
            service.put("ID", id);
            service.put("Service", name);
        }

        public ServiceBuilder address(String address) {
            service.put("Address", address);
            return this;
        }

        public ServiceBuilder port(int port) {
            service.put("Port", port);
            return this;
        }

        public ServiceBuilder tags(String... tags) {
            service.put("Tags", Arrays.asList(tags));
            return this;
        }

        public CheckBuilder check(String id, String name) {
            map.put("Service", service);
            return new CheckBuilder(id, name);
        }

        public void execute() {
            map.put("Service", service);
            CatalogRegisterBuilder.this.execute();
        }
    }

    public class CheckBuilder {
        private final Map<String, Object> check = new HashMap<>();

        private CheckBuilder(String id, String name) {
            check.put("CheckID", id);
            check.put("Name", name);
        }

        public CheckBuilder notes(String notes) {
            check.put("Notes", notes);
            return this;
        }

        public CheckBuilder status(String status) {
            check.put("Status", status);
            return this;
        }

        public CheckBuilder forService(String node, String serviceID) {
            check.put("Node", node);
            check.put("ServiceID", serviceID);
            return this;
        }

        public ServiceBuilder service(String id, String name) {
            map.put("Check", check);
            return new ServiceBuilder(id, name);
        }

        public void execute() {
            map.put("Check", check);
            CatalogRegisterBuilder.this.execute();
        }

    }

}