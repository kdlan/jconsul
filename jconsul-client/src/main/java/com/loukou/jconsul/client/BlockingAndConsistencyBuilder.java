package com.loukou.jconsul.client;

import com.loukou.jconsul.client.model.ConsistencyMode;

abstract class BlockingAndConsistencyBuilder<T extends BlockingAndConsistencyBuilder<T>> {
    private final Class<? extends T> subClass;
    protected final JConsulRequestBuilder requestBuilder;
    protected BlockingAndConsistencyBuilder(Class<? extends T> subClass,JConsulRequestBuilder requestBuilder) {
        this.subClass = subClass;
        this.requestBuilder=requestBuilder;
    }

    public T consistency(ConsistencyMode mode) {
        requestBuilder.addParameter(mode.name().toLowerCase());
        return subClass.cast(this);
    }

    public T datacenter(String datacenter) {
        requestBuilder.addParameter("dc", datacenter);
        return subClass.cast(this);
    }

    /**
     *
     * @param seconds
     *            wait seconds
     * @param index
     *            the request index
     * @return the builder object
     */
    public T poll(int seconds, long index) {
        requestBuilder.addParameter("wait", seconds);
        requestBuilder.addParameter("index", index);
        return subClass.cast(this);
    }

}
