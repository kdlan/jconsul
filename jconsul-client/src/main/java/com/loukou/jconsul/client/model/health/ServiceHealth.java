package com.loukou.jconsul.client.model.health;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ServiceHealth {

    @SerializedName("Node")
    private Node node;

    @SerializedName("Service")
    private Service service;

    @SerializedName("Checks")
    private List<HealthCheck> checks;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<HealthCheck> getChecks() {
        return checks;
    }

    public void setChecks(List<HealthCheck> checks) {
        this.checks = checks;
    }
}
