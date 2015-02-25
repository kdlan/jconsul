package com.loukou.jconsul.client.model.health;

import com.google.gson.annotations.SerializedName;


public class Node {

    @SerializedName("Node")
    private String node;

    @SerializedName("Address")
    private String address;

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
}
