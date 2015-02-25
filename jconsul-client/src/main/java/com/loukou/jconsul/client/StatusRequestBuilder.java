package com.loukou.jconsul.client;

import java.util.List;

import com.google.common.reflect.TypeToken;

public class StatusRequestBuilder extends JConsulRequestBuilder{

    StatusRequestBuilder(RequestProcessor processor) {
        super(processor);
    }

    public String leader(){
        return getJsonResult("GET", "/status/leader", String.class);
    }

    @SuppressWarnings("serial")
    public List<String> peers(){
        return getJsonResult("GET", "/status/peers", new TypeToken<List<String>>(){});
    }
}
