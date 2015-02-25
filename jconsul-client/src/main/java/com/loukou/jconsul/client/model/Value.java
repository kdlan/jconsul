package com.loukou.jconsul.client.model;

import com.google.gson.annotations.SerializedName;
import com.ning.http.util.Base64;

public class Value {

    @SerializedName("CreateIndex")
    private long createIndex;

    @SerializedName("ModifyIndex")
    private long modifyIndex;

    @SerializedName("LockIndex")
    private long lockIndex;

    @SerializedName("Key")
    private String key;

    @SerializedName("Flags")
    private long flags;

    @SerializedName("Value")
    private String value;

    @SerializedName("Session")
    private String session;

    public long getCreateIndex() {
        return createIndex;
    }

    public void setCreateIndex(long createIndex) {
        this.createIndex = createIndex;
    }

    public long getModifyIndex() {
        return modifyIndex;
    }

    public void setModifyIndex(long modifyIndex) {
        this.modifyIndex = modifyIndex;
    }

    public long getLockIndex() {
        return lockIndex;
    }

    public void setLockIndex(long lockIndex) {
        this.lockIndex = lockIndex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRawValue(){
        if(value==null){
            return null;
        }
        return new String(Base64.decode(value));
    }
}
