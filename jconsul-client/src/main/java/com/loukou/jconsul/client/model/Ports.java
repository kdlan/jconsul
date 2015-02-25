package com.loukou.jconsul.client.model;

import com.google.gson.annotations.SerializedName;

public class Ports {

    @SerializedName("DNS")
    private int dns;

    @SerializedName("HTTP")
    private int http;

    @SerializedName("RPC")
    private int rpc;

    @SerializedName("SerfLan")
    private int serfLan;

    @SerializedName("SerfWan")
    private int serfWan;

    @SerializedName("Server")
    private int server;

    public int getDns() {
        return dns;
    }

    public void setDns(int dns) {
        this.dns = dns;
    }

    public int getHttp() {
        return http;
    }

    public void setHttp(int http) {
        this.http = http;
    }

    public int getRpc() {
        return rpc;
    }

    public void setRpc(int rpc) {
        this.rpc = rpc;
    }

    public int getSerfLan() {
        return serfLan;
    }

    public void setSerfLan(int serfLan) {
        this.serfLan = serfLan;
    }

    public int getSerfWan() {
        return serfWan;
    }

    public void setSerfWan(int serfWan) {
        this.serfWan = serfWan;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }
}
