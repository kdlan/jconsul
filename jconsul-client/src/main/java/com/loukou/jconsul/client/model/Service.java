package com.loukou.jconsul.client.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Service {

    @SerializedName("ID")
    private String id;

    @SerializedName("Service")
    private String service;

    @SerializedName("Tags")
    private List<String> tags;

    @SerializedName("Port")
    private int port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
