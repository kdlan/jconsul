package com.loukou.jconsul.client.model.agent;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("Bootstrap")
    private boolean bootstrap;

    @SerializedName("BootstrapExpect")
    private boolean bootstrapExpect;

    @SerializedName("Server")
    private boolean server;

    @SerializedName("Datacenter")
    private String datacenter;

    @SerializedName("DataDir")
    private String dataDir;

    @SerializedName("DNSRecursor")
    private String dnsRecursor;

    @SerializedName("DNSRecursors")
    private List<String> dnsRecursors;

    @SerializedName("Domain")
    private String domain;

    @SerializedName("LogLevel")
    private String logLevel;

    @SerializedName("NodeName")
    private String nodeName;

    @SerializedName("ClientAddr")
    private String clientAddr;

    @SerializedName("BindAddr")
    private String bindAddr;

    @SerializedName("AdvertiseAddr")
    private String advertiseAddr;

    @SerializedName("Ports")
    private Ports ports;

    @SerializedName("LeaveOnTerm")
    private boolean leaveOnTerm;

    @SerializedName("SkipLeaveOnInt")
    private boolean skipLeaveOnInt;

    @SerializedName("StatsiteAddr")
    private String statsiteAddr;

    @SerializedName("Protocol")
    private int protocol;

    @SerializedName("EnableDebug")
    private boolean enableDebug;

    @SerializedName("VerifyIncoming")
    private boolean verifyIncoming;

    @SerializedName("VerifyOutgoing")
    private boolean verifyOutgoing;

    @SerializedName("CAFile")
    private String caFile;

    @SerializedName("CertFile")
    private String certFile;

    @SerializedName("KeyFile")
    private String keyFile;

    @SerializedName("StartJoin")
    private List<String> startJoin;

    @SerializedName("UiDir")
    private String uiDir;

    @SerializedName("PidFile")
    private String pidFile;

    @SerializedName("EnableSyslog")
    private boolean enableSyslog;

    @SerializedName("RejoinAfterLeave")
    private boolean rejoinAfterLeave;

    public boolean isBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(boolean bootstrap) {
        this.bootstrap = bootstrap;
    }

    public boolean isBootstrapExpect() {
        return bootstrapExpect;
    }

    public void setBootstrapExpect(boolean bootstrapExpect) {
        this.bootstrapExpect = bootstrapExpect;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getDnsRecursor() {
        return dnsRecursor;
    }

    public void setDnsRecursor(String dnsRecursor) {
        this.dnsRecursor = dnsRecursor;
    }

    public List<String> getDnsRecursors() {
        return dnsRecursors;
    }

    public void setDnsRecursors(List<String> dnsRecursors) {
        this.dnsRecursors = dnsRecursors;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public String getBindAddr() {
        return bindAddr;
    }

    public void setBindAddr(String bindAddr) {
        this.bindAddr = bindAddr;
    }

    public String getAdvertiseAddr() {
        return advertiseAddr;
    }

    public void setAdvertiseAddr(String advertiseAddr) {
        this.advertiseAddr = advertiseAddr;
    }

    public Ports getPorts() {
        return ports;
    }

    public void setPorts(Ports ports) {
        this.ports = ports;
    }

    public boolean isLeaveOnTerm() {
        return leaveOnTerm;
    }

    public void setLeaveOnTerm(boolean leaveOnTerm) {
        this.leaveOnTerm = leaveOnTerm;
    }

    public boolean isSkipLeaveOnInt() {
        return skipLeaveOnInt;
    }

    public void setSkipLeaveOnInt(boolean skipLeaveOnInt) {
        this.skipLeaveOnInt = skipLeaveOnInt;
    }

    public String getStatsiteAddr() {
        return statsiteAddr;
    }

    public void setStatsiteAddr(String statsiteAddr) {
        this.statsiteAddr = statsiteAddr;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public boolean isVerifyIncoming() {
        return verifyIncoming;
    }

    public void setVerifyIncoming(boolean verifyIncoming) {
        this.verifyIncoming = verifyIncoming;
    }

    public boolean isVerifyOutgoing() {
        return verifyOutgoing;
    }

    public void setVerifyOutgoing(boolean verifyOutgoing) {
        this.verifyOutgoing = verifyOutgoing;
    }

    public String getCaFile() {
        return caFile;
    }

    public void setCaFile(String caFile) {
        this.caFile = caFile;
    }

    public String getCertFile() {
        return certFile;
    }

    public void setCertFile(String certFile) {
        this.certFile = certFile;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public List<String> getStartJoin() {
        return startJoin;
    }

    public void setStartJoin(List<String> startJoin) {
        this.startJoin = startJoin;
    }

    public String getUiDir() {
        return uiDir;
    }

    public void setUiDir(String uiDir) {
        this.uiDir = uiDir;
    }

    public String getPidFile() {
        return pidFile;
    }

    public void setPidFile(String pidFile) {
        this.pidFile = pidFile;
    }

    public boolean isEnableSyslog() {
        return enableSyslog;
    }

    public void setEnableSyslog(boolean enableSyslog) {
        this.enableSyslog = enableSyslog;
    }

    public boolean isRejoinAfterLeave() {
        return rejoinAfterLeave;
    }

    public void setRejoinAfterLeave(boolean rejoinAfterLeave) {
        this.rejoinAfterLeave = rejoinAfterLeave;
    }

}
