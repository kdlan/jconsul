package com.loukou.jconsul.client;

import java.util.List;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.util.JConsulUtils;
import com.google.common.base.Optional;
import com.google.common.reflect.TypeToken;

abstract class UnwrapListResultBuilder<T extends UnwrapListResultBuilder<T, V>, V> extends
        BlockingAndConsistencyBuilder<UnwrapListResultBuilder<T, V>> implements ResultRequestBuilder<V> {

    private final String path;
    private TypeToken<List<V>> type;

    protected UnwrapListResultBuilder(String path, TypeToken<List<V>> type,
            Class<? extends UnwrapListResultBuilder<T, V>> subClass, JConsulRequestBuilder requestBuilder) {
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
        JConsulResponse<List<V>> response = requestBuilder.getJConsulResponse("GET", path, type);

        return JConsulUtils.transform(response);
    }

    @Override
    public void async(final JConsulResponseCallback<V> callback) {
        requestBuilder.executeResult("GET", path, type, JConsulUtils.transform(callback));
    }
}
