package com.loukou.jconsul.client.model.agent;

import com.google.gson.annotations.SerializedName;

public class Agent {

    @SerializedName("Config")
    private Config config;

    @SerializedName("Member")
    private Member member;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


}