package com.loukou.jconsul.client.model.agent;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ServiceRegistration {

    @SerializedName("ID")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Address")
    private String address;

    @SerializedName("Port")
    private int port;

    @SerializedName("Check")
    private Check check;

    @SerializedName("Tags")
    private List<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static class Check {

        @SerializedName("Script")
        private String script;

        @SerializedName("HTTP")
        private String http;

        @SerializedName("Timeout")
        private String timeout;

        @SerializedName("Interval")
        private String interval;

        @SerializedName("TTL")
        private String ttl;

        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public String getHttp() {
            return http;
        }

        public void setHttp(String http) {
            this.http = http;
        }

        public String getTimeout() {
            return timeout;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

    }

}
