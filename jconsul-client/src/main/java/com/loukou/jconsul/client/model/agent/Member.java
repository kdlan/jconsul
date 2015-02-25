package com.loukou.jconsul.client.model.agent;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("Name")
    private String name;

    @SerializedName("Addr")
    private String addr;

    @SerializedName("Port")
    private int port;

    @SerializedName("Tags")
    private Map<String, String> tags;

    @SerializedName("Status")
    private int status;

    @SerializedName("ProtocolMin")
    private int protocolMin;

    @SerializedName("ProtocolMax")
    private int protocolMax;

    @SerializedName("ProtocolCur")
    private int protocolCur;

    @SerializedName("DelegateMin")
    private int delegateMin;

    @SerializedName("DelegateMax")
    private int delegateMax;

    @SerializedName("DelegateCur")
    private int delegateCur;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProtocolMin() {
        return protocolMin;
    }

    public void setProtocolMin(int protocolMin) {
        this.protocolMin = protocolMin;
    }

    public int getProtocolMax() {
        return protocolMax;
    }

    public void setProtocolMax(int protocolMax) {
        this.protocolMax = protocolMax;
    }

    public int getProtocolCur() {
        return protocolCur;
    }

    public void setProtocolCur(int protocolCur) {
        this.protocolCur = protocolCur;
    }

    public int getDelegateMin() {
        return delegateMin;
    }

    public void setDelegateMin(int delegateMin) {
        this.delegateMin = delegateMin;
    }

    public int getDelegateMax() {
        return delegateMax;
    }

    public void setDelegateMax(int delegateMax) {
        this.delegateMax = delegateMax;
    }

    public int getDelegateCur() {
        return delegateCur;
    }

    public void setDelegateCur(int delegateCur) {
        this.delegateCur = delegateCur;
    }
}
