package com.loukou.jconsul.client.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("CreateIndex")
    private long createIndex;

    @SerializedName("ID")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Node")
    private String node;

    @SerializedName("Checks")
    private List<String> checks;

    @SerializedName("LockDelay")
    private long lockDelay;

    @SerializedName("Behavior")
    private String behavior;

    @SerializedName("TTL")
    private String ttl;

    public long getCreateIndex() {
        return createIndex;
    }

    public void setCreateIndex(long createIndex) {
        this.createIndex = createIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public List<String> getChecks() {
        return checks;
    }

    public void setChecks(List<String> checks) {
        this.checks = checks;
    }

    public long getLockDelay() {
        return lockDelay;
    }

    public void setLockDelay(long lockDelay) {
        this.lockDelay = lockDelay;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

}
