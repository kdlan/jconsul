package com.loukou.jconsul.client;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.google.common.base.Optional;
import com.google.common.reflect.TypeToken;

class DirectResultBuilder<T extends DirectResultBuilder<T, V>, V> extends
        BlockingAndConsistencyBuilder<DirectResultBuilder<T, V>> implements ResultRequestBuilder<V> {

    private final String path;
    private TypeToken<V> type;

    public DirectResultBuilder(String path, TypeToken<V> type, Class<? extends DirectResultBuilder<T, V>> subClass,
            JConsulRequestBuilder requestBuilder) {
        super(subClass, requestBuilder);
        this.path = path;
        this.type = type;
    }

    @Override
    public Optional<V> value() {
        return response().getResult();
    }

    @Override
    public JConsulResponse<V> response() {
        return requestBuilder.getJConsulResponse("GET", path, type);
    }

    @Override
    public void async(JConsulResponseCallback<V> callback) {
        requestBuilder.executeResult("GET", path, type, callback);
    }
}
