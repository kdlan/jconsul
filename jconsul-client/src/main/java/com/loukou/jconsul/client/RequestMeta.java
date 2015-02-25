package com.loukou.jconsul.client;

import java.util.LinkedHashMap;
import java.util.Map;

class RequestMeta {

    private Map<String, Object> parameters = new LinkedHashMap<>();

    private Object body;

    private boolean json;

    void addParameter(String key) {
        parameters.put(key, null);
    }

    void addParameter(String key, Object value) {
        parameters.put(key, value);

    }

    void setStringBody(String body) {
        this.body = body;
        this.json = false;
    }

    void setJsonBody(Object data) {
        this.body = data;
        this.json = true;
    }

    Map<String, Object> getParameters() {
        return parameters;
    }

    Object getBody() {
        return body;
    }

    boolean isJson() {
        return json;
    }

}
