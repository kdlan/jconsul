package com.loukou.jconsul.recipes;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.JConsulException;
import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.Value;

public class SharedMap {

    private final JConsul jconsul;

    private final LoadingCache<String, Optional<Value>> map;

    public SharedMap() {
        this(new JConsul());
    }

    public SharedMap(JConsul jconsul) {
        this.jconsul = jconsul;
        map = CacheBuilder.newBuilder().<String, Optional<Value>> build(new CacheLoader<String, Optional<Value>>() {
            @Override
            public Optional<Value> load(String key) throws Exception {
                JConsulResponse<Value> result = SharedMap.this.jconsul.keyValue().get(key).response();

                Optional<Value> value=result.getResult();

                SharedMap.this.jconsul.keyValue().get(key).datacenter("123");

                return value;
            }
        });
    }

    public Value get(String key) {

        try {
            return map.get(key).orNull();
        } catch (ExecutionException e) {
            throw new JConsulException(e);
        }
    }

}
