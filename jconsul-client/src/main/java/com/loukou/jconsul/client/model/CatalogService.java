package com.loukou.jconsul.client.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CatalogService {

    @SerializedName("Node")
    private String node;

    @SerializedName("Address")
    private String address;

    @SerializedName("ServiceName")
    private String serviceName;

    @SerializedName("ServiceID")
    private String serviceId;

    @SerializedName("ServiceAddress")
    private String serviceAddress;

    @SerializedName("ServicePort")
    private int servicePort;

    @SerializedName("ServiceTags")
    private List<String> serviceTags;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<String> getServiceTags() {
        return serviceTags;
    }

    public void setServiceTags(List<String> serviceTags) {
        this.serviceTags = serviceTags;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

}
