package com.loukou.jconsul.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.loukou.jconsul.client.model.Session;

public class SessionRequestBuilder extends JConsulRequestBuilder {

    @SuppressWarnings("serial")
    private static final TypeToken<List<Session>> LIST_SESSION_TOKEN = new TypeToken<List<Session>>() {
    };

    SessionRequestBuilder(RequestProcessor processor) {
        super(processor);
    }

    public SessionCreateBuilder create() {
        return new SessionCreateBuilder();
    }

    public SessionInfoBuilder info(String session) {
        return new SessionInfoBuilder(session);
    }

    public SessionNodeBuilder node(String node) {
        return new SessionNodeBuilder(node);
    }

    public SessionListBuilder list() {
        return new SessionListBuilder();
    }

    public class SessionCreateBuilder {
        private final Map<String, Object> map = Maps.newHashMap();

        private SessionCreateBuilder() {
        }

        public SessionCreateBuilder lockDelay(int seconds) {
            map.put("LockDelay", seconds + "s");
            return this;
        }

        public SessionCreateBuilder name(String name) {
            map.put("Name", name);
            return this;
        }

        public SessionCreateBuilder node(String node) {
            map.put("Node", node);
            return this;
        }

        public SessionCreateBuilder checks(String... checks) {
            map.put("Checks", Arrays.asList(checks));
            return this;
        }

        public SessionCreateBuilder behavior(String behavior) {
            map.put("Behavior", behavior);
            return this;
        }

        public SessionCreateBuilder ttl(int ttlSeconds) {
            map.put("TTL", ttlSeconds + "s");
            return this;
        }

        public SessionStatus execute() {
            setJsonBody(map);
            Session session = getJsonResult("PUT", "/session/create", Session.class);
            return new SessionStatus(session.getId(), SessionRequestBuilder.this);
        }
    }

    public static class SessionStatus {
        private final String id;
        private final JConsulRequestBuilder builder;

        public SessionStatus(String id, JConsulRequestBuilder builder) {
            this.id = id;
            this.builder = new JConsulRequestBuilder(builder.getProcessor());
        }

        public String getId() {
            return id;
        }

        public void destroy() {
            builder.getPlainResult("PUT", "/session/destroy/" + id);
            builder.reset();
        }

        public Session renew() {
            List<Session> result = builder.getJsonResult("PUT", "/session/renew/" + id, LIST_SESSION_TOKEN);
            builder.reset();
            return Iterables.getOnlyElement(result);
        }

    }

    public class SessionInfoBuilder extends UnwrapListResultBuilder<SessionInfoBuilder, Session> {

        private SessionInfoBuilder(String id) {
            super("/session/info/" + id, LIST_SESSION_TOKEN, SessionInfoBuilder.class, SessionRequestBuilder.this);
        }

    }

    public class SessionNodeBuilder extends DirectResultBuilder<SessionListBuilder, List<Session>> {
        private SessionNodeBuilder(String node) {
            super("/session/node/" + node, LIST_SESSION_TOKEN, SessionListBuilder.class, SessionRequestBuilder.this);
        }
    }

    public class SessionListBuilder extends DirectResultBuilder<SessionListBuilder, List<Session>> {
        private SessionListBuilder() {
            super("/session/list", LIST_SESSION_TOKEN, SessionListBuilder.class, SessionRequestBuilder.this);
        }
    }
}
