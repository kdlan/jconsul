package com.loukou.jconsul.client;

import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loukou.jconsul.client.model.JConsulResponse;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.Futures;
import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

class RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

    private final String baseUrl;
    private final Gson gson;
    private final AsyncHttpClient client;

    RequestProcessor(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new Gson();
        // As consul should run on localhost, set default connect timeout to
        // 50ms and request timeout to 200ms
        AsyncHttpClientConfig clientConfig = new AsyncHttpClientConfig.Builder().setConnectTimeout(500)
                .setRequestTimeout(2000).build();
        this.client = new AsyncHttpClient(clientConfig);
    }

    private Request buildRequest(String method, String path, RequestMeta meta) {
        RequestBuilder builder = new RequestBuilder();

        builder.setMethod(method);
        builder.setUrl(baseUrl + path);
        for (Entry<String, Object> entry : meta.getParameters().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("wait".equals(key)) {
                Integer wait = (Integer) value;
                builder.setRequestTimeout((wait + 1) * 1000);
                value = value + "s";
            }
            String param;
            if (value == null) {
                param = null;
            } else {
                param = String.valueOf(value);
            }

            builder.addQueryParam(key, param);
        }

        Object body = meta.getBody();
        if (body != null) {
            if (meta.isJson()) {
                builder.setBody(gson.toJson(body));
            } else {
                builder.setBody(body.toString());
            }
        }

        Request request = builder.build();
        return request;
    }

    private <T> Future<T> execute(final Request request, final ResultResolver<T> resolver) {
        return client.executeRequest(request, new AsyncCompletionHandler<T>() {
            public T onCompleted(Response response) throws Exception {
                String body = response.getResponseBody();
                int statusCode = response.getStatusCode();
                LOGGER.debug("Request {}, method {}, response code {}, body is\n{}", request.getUrl(),
                        request.getMethod(), statusCode, body);
                if (statusCode / 100 == 5) {
                    throw new JConsulException("Response http code " + statusCode + ", msg: " + body);
                }
                T result = resolver.resolve(response, response.getResponseBody());
                return result;
            }

            @Override
            public void onThrowable(Throwable throwable) {
                resolver.onFailure(throwable);
            }
        });
    }

    private <T> T executeResult(String method, String path, RequestMeta meta, final ResultResolver<T> resolver) {
        Request request = buildRequest(method, path, meta);
        Future<T> result = execute(request, resolver);
        return Futures.getUnchecked(result);
    }

    public <T> T getJsonResult(String method, String path, RequestMeta meta, TypeToken<T> type) {
        return executeResult(method, path, meta, new GsonResultResolver<T>(gson, type));
    }

    public String getPlainResult(String method, String path, RequestMeta meta) {
        return executeResult(method, path, meta, new PlainResultResolver());
    }

    public <T> JConsulResponse<T> getResponse(String method, String path, RequestMeta meta, TypeToken<T> type) {
        return executeResult(method, path, meta, new ResponseResultResolver<T>(gson, type));
    }

    public <T> JConsulResponse<String> getRawResponse(String method, String path, RequestMeta meta) {
        return executeResult(method, path, meta, new ResponseResultResolver<String>());
    }

    public void asyncGetRawResponse(String method, String path, RequestMeta meta,
            JConsulResponseCallback<String> callback) {
        Request request = buildRequest(method, path, meta);
        execute(request, new CallbackResponseResultResolver<String>(callback, new PlainResultResolver()));
    }

    public <T> void asyncGetResponse(String method, String path, RequestMeta meta, TypeToken<T> type,
            JConsulResponseCallback<T> callback) {
        Request request = buildRequest(method, path, meta);
        execute(request, new CallbackResponseResultResolver<T>(callback, new GsonResultResolver<T>(gson, type)));
    }

    private static <T> JConsulResponse<T> buildResponse(T result, Response response, String responseBody) {
        long lastContact = Long.parseLong(response.getHeader("X-Consul-Lastcontact"));
        boolean knownLeader = Boolean.parseBoolean(response.getHeader("X-Consul-Knownleader"));
        long index = Long.parseLong(response.getHeader("X-Consul-Index"));

        JConsulResponse<T> consulResponse = new JConsulResponse<T>(result, lastContact, knownLeader, index);
        return consulResponse;
    }

    private interface ResultResolver<T> {

        T resolve(Response response, String responseBody);

        void onFailure(Throwable throwable);
    }

    private static class GsonResultResolver<T> implements ResultResolver<T> {

        private final Gson gson;
        private final TypeToken<T> type;

        public GsonResultResolver(Gson gson, TypeToken<T> type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public T resolve(Response response, String responseBody) {
            T result = gson.fromJson(responseBody, type.getType());
            return result;
        }

        @Override
        public void onFailure(Throwable throwable) {
            throw new JConsulException(throwable);
        }

    }

    private static class PlainResultResolver implements ResultResolver<String> {

        @Override
        public String resolve(Response response, String responseBody) {
            return responseBody;
        }

        @Override
        public void onFailure(Throwable throwable) {
            throw new JConsulException(throwable);
        }

    }

    private static class ResponseResultResolver<T> implements ResultResolver<JConsulResponse<T>> {
        private final ResultResolver<T> resolver;

        public ResponseResultResolver(Gson gson, TypeToken<T> type) {
            resolver = new GsonResultResolver<T>(gson, type);
        }

        @SuppressWarnings("unchecked")
        public ResponseResultResolver() {
            resolver = ResultResolver.class.cast(new PlainResultResolver());
        }

        @Override
        public JConsulResponse<T> resolve(Response response, String responseBody) {
            T result;
            if (response.getStatusCode() == 404) {
                result = null;
            } else {

                result = resolver.resolve(response, responseBody);
            }
            return buildResponse(result, response, responseBody);
        }

        @Override
        public void onFailure(Throwable throwable) {
            throw new JConsulException(throwable);
        }

    }

    private static class CallbackResponseResultResolver<T> implements ResultResolver<T> {
        private final JConsulResponseCallback<T> callback;
        private final ResultResolver<T> resolver;

        public CallbackResponseResultResolver(JConsulResponseCallback<T> callback, ResultResolver<T> resolver) {
            this.callback = callback;
            this.resolver = resolver;
        }

        @Override
        public T resolve(Response response, String responseBody) {
            T result;
            if (response.getStatusCode() == 404) {
                result = null;
            } else {

                result = resolver.resolve(response, responseBody);
            }
            callback.onComplete(buildResponse(result, response, responseBody));
            return result;
        }

        @Override
        public void onFailure(Throwable throwable) {
            callback.onFailure(throwable);
        }

    }

    public void close() {
        client.close();
    }

}
