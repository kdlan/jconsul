package com.loukou.jconsul.client;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.google.common.reflect.TypeToken;

class JConsulRequestBuilder {

    private final RequestProcessor processor;
    private RequestMeta meta;

    JConsulRequestBuilder(RequestProcessor processor) {
        this.processor = processor;
        meta = new RequestMeta();

    }

    JConsulRequestBuilder(JConsulRequestBuilder builder) {
        this.processor = builder.processor;
        this.meta = builder.meta;

    }

    RequestProcessor getProcessor() {
        return processor;
    }

    final void addParameter(String key) {
        meta.addParameter(key);
    }

    final void addParameter(String key, Object value) {
        meta.addParameter(key, value);

    }

    final void setStringBody(String body) {
        meta.setStringBody(body);
    }

    final void setJsonBody(Object body) {
        meta.setJsonBody(body);
    }

    protected final <T> T getJsonResult(String method, String path, Class<T> clazz) {
        return getJsonResult(method, path, TypeToken.of(clazz));
    }

    protected final <T> T getJsonResult(String method, String path, TypeToken<T> type) {
        return processor.getJsonResult(method, path, meta, type);
    }

    protected final String getPlainResult(String method, String path) {
        return processor.getPlainResult(method, path, meta);
    }

    protected final <T> JConsulResponse<T> getJConsulResponse(String method, String path, Class<T> clazz) {
        return getJConsulResponse(method, path, TypeToken.of(clazz));
    }

    protected final <T> JConsulResponse<T> getJConsulResponse(String method, String path, TypeToken<T> type) {
        return processor.getResponse(method, path, meta, type);
    }

    protected final JConsulResponse<String> getRawJConsulResponse(String method, String path) {
        return processor.getRawResponse(method, path, meta);
    }

    protected final void executeRaw(String method, String path, JConsulResponseCallback<String> callback) {
        processor.asyncGetRawResponse(method, path, meta, callback);
    }

    protected final <T> void executeResult(String method, String path, TypeToken<T> type,
            JConsulResponseCallback<T> callback) {
        processor.asyncGetResponse(method, path, meta, type, callback);
    }

    protected void reset(){
        meta=new RequestMeta();
    }
}
