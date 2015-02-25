package com.loukou.jconsul.client;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.google.common.base.Optional;

public interface ResultRequestBuilder<V> {
    public Optional<V> value() ;

    public JConsulResponse<V> response() ;

    public void async(final JConsulResponseCallback<V> callback);
}
