package com.loukou.jconsul.client;

import java.util.List;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.loukou.jconsul.client.model.kv.Value;
import com.google.common.base.Optional;
import com.google.common.reflect.TypeToken;

public class KeyValueRequestBuilder extends JConsulRequestBuilder {
    private static final String KV_PATH = "/kv";
    @SuppressWarnings("serial")
    private static final TypeToken<List<Value>> LIST_VALUE_TOKEN = new TypeToken<List<Value>>() {
    };

    @SuppressWarnings("serial")
    private static final TypeToken<List<String>> LIST_STRING_TOKEN = new TypeToken<List<String>>() {
    };

    KeyValueRequestBuilder(RequestProcessor processor) {
        super(processor);

    }

    public ValueRequestBuilder get(String key) {
        return new ValueRequestBuilder(key);
    }

    public KeysRequestBuilder keys(String key) {
        return new KeysRequestBuilder(key);
    }

    public RecurseRequestBuilder recurse(String key) {
        return new RecurseRequestBuilder(key);
    }

    public PutRequestBuilder put(String key) {
        return new PutRequestBuilder(key);
    }

    public DeleteRequestBuilder delete(String key) {
        return new DeleteRequestBuilder(key);
    }

    public class ValueRequestBuilder extends UnwrapListResultBuilder<ValueRequestBuilder, Value> {
        private final String key;

        private ValueRequestBuilder(String key) {
            super(buildPath(key), LIST_VALUE_TOKEN, ValueRequestBuilder.class, KeyValueRequestBuilder.this);
            this.key = key;
        }

        public RawRequestBuilder raw() {
            return new RawRequestBuilder(key);
        }

    }

    public class RawRequestBuilder extends BlockingAndConsistencyBuilder<RawRequestBuilder> implements
            ResultRequestBuilder<String> {
        private final String key;

        private RawRequestBuilder(String key) {
            super(RawRequestBuilder.class, KeyValueRequestBuilder.this);
            this.key = key;
            addParameter("raw");
        }

        @Override
        public Optional<String> value() {
            return response().getResult();
        }

        @Override
        public void async(JConsulResponseCallback<String> callback) {
            executeRaw("GET", buildPath(key), callback);
        }

        @Override
        public JConsulResponse<String> response() {

            return getRawJConsulResponse("GET", buildPath(key));
        }

    }

    public class KeysRequestBuilder extends DirectResultBuilder<KeysRequestBuilder, List<String>> {
        private KeysRequestBuilder(String key) {
            super(buildPath(key), LIST_STRING_TOKEN, KeysRequestBuilder.class, KeyValueRequestBuilder.this);
            addParameter("keys");
        }

        public KeysRequestBuilder seperator(String seperator) {
            addParameter("seperator", seperator);
            return this;
        }

    }

    public class RecurseRequestBuilder extends DirectResultBuilder<RecurseRequestBuilder, List<Value>> {
        private RecurseRequestBuilder(String key) {
            super(buildPath(key), LIST_VALUE_TOKEN, RecurseRequestBuilder.class, KeyValueRequestBuilder.this);
            addParameter("recurse");
        }
    }

    public class PutRequestBuilder {

        private final String key;

        private PutRequestBuilder(String key) {
            this.key = key;
        }

        public PutRequestBuilder datacenter(String datacenter) {
            addParameter("dc", datacenter);
            return this;
        }

        public PutRequestBuilder flags(long flags) {
            addParameter("flags", flags);
            return this;
        }

        public PutRequestBuilder cas(long index) {
            addParameter("cas", index);
            return this;
        }

        public PutRequestBuilder acquire(String session) {
            addParameter("acquire", session);
            return this;
        }

        public PutRequestBuilder release(String session) {
            addParameter("release", session);
            return this;
        }

        public boolean value(String value) {
            setStringBody(value);
            return value();
        }

        public boolean value() {
            String result = getPlainResult("PUT", buildPath(key));
            return Boolean.parseBoolean(result);
        }
    }

    public class DeleteRequestBuilder {
        private final String key;

        public DeleteRequestBuilder(String key) {
            super();
            this.key = key;
        }

        public DeleteRequestBuilder recurse() {
            addParameter("recurse");
            return this;
        }

        public DeleteRequestBuilder cas(long index) {
            addParameter("cas", index);
            return this;
        }


        public boolean value() {
            String result = getPlainResult("DELETE", buildPath(key));
            return Boolean.parseBoolean(result);
        }

    }

    private String buildPath(String key) {
        return KV_PATH + attachAheadSlash(key);
    }

    private String attachAheadSlash(String value) {
        if (value.startsWith("/")) {
            return value;
        } else {
            return "/" + value;
        }
    }

}
