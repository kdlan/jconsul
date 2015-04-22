package com.loukou.jconsul.recipes;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.loukou.jconsul.client.JConsul;
import com.loukou.jconsul.client.JConsulException;
import com.loukou.jconsul.client.JConsulResponseCallback;
import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.Value;

public class SharedMap {
    private static Logger LOG = LoggerFactory.getLogger(SharedMap.class);

    private final JConsul jconsul;

    private final LoadingCache<String, Optional<String>> map;

    private final Map<String, Set<ValueListener<String>>> listeners;

    private ExecutorService pool = Executors.newCachedThreadPool();

    public SharedMap() {
        this(new JConsul());
    }

    public SharedMap(JConsul jconsul) {
        this.jconsul = jconsul;
        map = CacheBuilder.newBuilder().<String, Optional<String>> build(new CacheLoader<String, Optional<String>>() {
            @Override
            public Optional<String> load(String key) throws Exception {
                JConsulResponse<Value> result = SharedMap.this.jconsul.keyValue().get(key).response();
                processResponse(key, 0, result);
                return result.getResult().transform(new Function<Value, String>() {
                    @Override
                    public String apply(Value input) {
                        return input.getRawValue();
                    }
                });
            }
        });
        listeners = CacheBuilder.newBuilder()
                .<String, Set<ValueListener<String>>> build(new CacheLoader<String, Set<ValueListener<String>>>() {
                    @Override
                    public Set<ValueListener<String>> load(String key) throws Exception {
                        return Sets.newConcurrentHashSet();
                    }
                }).asMap();
    }

    public void addListener(String key, ValueListener<String> listener) {
        listeners.get(key).add(listener);
    }

    public String get(String key) {
        try {
            return map.get(key).orNull();
        } catch (ExecutionException e) {
            throw new JConsulException(e);
        }
    }

    public boolean put(String key, String value) {
        map.put(key, Optional.of(value));
        return jconsul.keyValue().put(key).value(value);
    }

    private void processResponse(final String key, long requestIndex, JConsulResponse<Value> response) {
        final Optional<Value> result = response.getResult();

        final long newIndex = result.isPresent() ? result.get().getModifyIndex() : response.getIndex();

        if (newIndex > requestIndex && listeners.containsKey(key)) {
            for (final ValueListener<String> listener : listeners.get(key)) {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Value value = result.orNull();
                        listener.onChange(value == null ? null : value.getRawValue());
                    }
                });
            }
        }
        jconsul.keyValue().get(key).poll(3, newIndex).async(new JConsulResponseCallback<Value>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOG.warn(throwable.getMessage(), throwable);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage(), e);
                }
            }

            @Override
            public void onComplete(JConsulResponse<Value> consulResponse) {
                Optional<Value> value = consulResponse.getResult();
                map.put(key, value.transform(new Function<Value, String>() {
                    @Override
                    public String apply(Value input) {
                        return input.getRawValue();
                    }
                }));
                processResponse(key, newIndex, consulResponse);
            }
        });
    }
}
