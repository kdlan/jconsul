package com.loukou.jconsul.client;

import java.net.MalformedURLException;
import java.net.URL;

public class JConsul {

    private final RequestProcessor processor;

    public JConsul() {
        this("127.0.0.1", 8500);
    }

    public JConsul(String host, int port) {
        try {
            URL url = new URL("http", host, port, "/v1");
            String baseUrl = url.toString();
            processor = new RequestProcessor(baseUrl);
        } catch (MalformedURLException e) {
            throw new JConsulException(e);
        }

    }

    public StatusRequestBuilder status() {
        return new StatusRequestBuilder(processor);
    }

    public KeyValueRequestBuilder keyValue() {
        return new KeyValueRequestBuilder(processor);
    }

    public AgentRequestBuilder agent() {
        return new AgentRequestBuilder(processor);
    }

    public CatalogRequestBuilder catalog(){
        return new CatalogRequestBuilder(processor);
    }

    public void close() {
        processor.close();
    }

}
