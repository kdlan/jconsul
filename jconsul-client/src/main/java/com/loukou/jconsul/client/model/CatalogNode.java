package com.loukou.jconsul.client.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class CatalogNode {

    @SerializedName("Node")
    private Node node;

    @SerializedName("Services")
    private Map<String, Service> services;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }
}
